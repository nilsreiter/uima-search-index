package de.unistuttgart.ims.uimautil.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class JCasSearchIndex<T extends Annotation> {
	Class<T> baseClass;

	Map<String, Map<String, Collection<T>>> indexes = new HashMap<String, Map<String, Collection<T>>>();

	String[] featureNames = new String[] { "/lemma/value" };

	public JCasSearchIndex(Class<T> baseClass) {
		this.baseClass = baseClass;
		for (String f : featureNames) {
			indexes.put(f, new HashMap<String, Collection<T>>());
		}
	}

	public List<Finding<T>> get(FeatureValueSearchTerm<T>... terms) throws ClassNotFoundException, CASException {
		if (terms.length == 0)
			return Collections.emptyList();

		Finding<T> currentFinding;
		if (!indexes.containsKey(terms[0].getFeaturePath()))
			return Collections.emptyList();
		if (!indexes.get(terms[0].getFeaturePath()).containsKey(terms[0].getValue()))
			return Collections.emptyList();
		List<Finding<T>> ret = new LinkedList<Finding<T>>();
		for (T anno : indexes.get(terms[0].getFeaturePath()).get(terms[0].getValue())) {
			JCas jcas = anno.getCAS().getJCas();
			currentFinding = new Finding<T>(anno);
			T nextAnnotation = anno;

			for (int i = 1; i < terms.length; i++) {

				nextAnnotation = JCasUtil.selectFollowing(baseClass, nextAnnotation, 1).get(0);
				FeaturePath path = jcas.createFeaturePath();
				path.initialize(terms[i].getFeaturePath());
				path.typeInit(jcas.getTypeSystem().getType(baseClass.getCanonicalName()));
				if (path.getValueAsString(nextAnnotation).equals(terms[i].getValue())) {
					currentFinding.getFindings().add(nextAnnotation);
				} else {
					currentFinding = null;
				}
			}
			if (currentFinding != null)
				ret.add(currentFinding);
		}
		return ret;
	}

	public void index(JCas jcas) throws ClassNotFoundException, CASException {
		System.err.println("Indexing jcas ...");
		long indexStartTime = System.currentTimeMillis();
		for (String feature : indexes.keySet()) {
			FeaturePath path = jcas.createFeaturePath();
			path.initialize(feature);
			path.typeInit(jcas.getTypeSystem().getType(baseClass.getName()));
			for (T annotation : JCasUtil.select(jcas, baseClass)) {
				String value = path.getValueAsString(annotation);
				if (!indexes.get(feature).containsKey(value)) {
					indexes.get(feature).put(value, new HashSet<T>());
				}
				indexes.get(feature).get(value).add(annotation);
			}
		}
		long indexingTime = System.currentTimeMillis() - indexStartTime;
		System.err.println("Indexing finished, took " + indexingTime + " ms.");
	}
}
