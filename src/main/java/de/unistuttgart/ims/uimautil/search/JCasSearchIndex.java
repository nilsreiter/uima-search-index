package de.unistuttgart.ims.uimautil.search;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FSStringConstraint;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

/**
 * Builds the main search index over the annotations of type <code>T</code>.
 * 
 * .
 * 
 * @author reiterns
 *
 * @param <T>
 */
public class JCasSearchIndex<T extends Annotation> {
	Logger logger = Logger.getLogger(JCasSearchIndex.class.getName());

	Class<T> baseClass;
	String baseClassName;

	ConstraintFactory cFactory = ConstraintFactory.instance();

	Map<String, Map<String, List<T>>> indexes = null;
	List<String> featureNames = new LinkedList<String>();

	JCas jcas = null;

	public JCasSearchIndex(Class<T> baseClass) {
		this.baseClass = baseClass;
		this.baseClassName = baseClass.getName();
	}

	public void addIndexFeatureName(String path) {
		featureNames.add(path);
	}

	public List<String> getIndexFeatureNames() {
		return featureNames;
	}

	@SuppressWarnings("unchecked")
	public List<Finding<T>> get(String query) throws CASException, ClassNotFoundException, IOException {
		QueryParser parser = new QueryParser();
		return get((SearchTerm<T>[]) parser.parse(query));
	}

	public List<Finding<T>> get(SearchTerm<T>... terms) throws ClassNotFoundException, CASException {
		logger.info("Received query: " + ArrayUtils.toString(terms));
		if (terms.length == 0)
			return Collections.emptyList();

		if (!indexes.containsKey(terms[0].getFeaturePath()))
			return Collections.emptyList();
		if (!indexes.get(terms[0].getFeaturePath()).containsKey(terms[0].getValue()))
			return Collections.emptyList();

		FSMatchConstraint[] constraints = new FSMatchConstraint[terms.length];
		for (int i = 0; i < terms.length; i++) {
			FeaturePath path = jcas.createFeaturePath();
			path.initialize(terms[i].getFeaturePath());
			path.typeInit(jcas.getTypeSystem().getType(baseClass.getCanonicalName()));
			FSStringConstraint constraint = cFactory.createStringConstraint();
			constraint.equals(terms[i].getValue());
			constraints[i] = cFactory.embedConstraint(path, constraint);
		}

		List<Finding<T>> ret = new LinkedList<Finding<T>>();
		Finding<T> currentFinding;
		for (T anno : indexes.get(terms[0].getFeaturePath()).get(terms[0].getValue())) {
			currentFinding = new Finding<T>(anno);
			T nextAnnotation = anno;

			for (int i = 1; i < terms.length; i++) {

				nextAnnotation = JCasUtil.selectFollowing(baseClass, nextAnnotation, 1).get(0);

				if (constraints[i].match(nextAnnotation)) {
					currentFinding.getFindings().add(nextAnnotation);
				} else {
					currentFinding = null;
				}
			}
			if (currentFinding != null) {
				ret.add(currentFinding);
				currentFinding = null;
			}
		}
		return ret;
	}

	public void index(JCas jcas) throws ClassNotFoundException, CASException {
		if (indexes == null) {
			initialiseIndexes();
		}
		this.jcas = jcas;
		logger.info("Indexing jcas ...");
		long indexStartTime = System.currentTimeMillis();
		FeaturePath path = jcas.createFeaturePath();
		for (String feature : indexes.keySet()) {
			path.initialize(feature);
			path.typeInit(jcas.getTypeSystem().getType(baseClassName));
			for (T annotation : JCasUtil.select(jcas, baseClass)) {
				String value = path.getValueAsString(annotation);
				if (!indexes.get(feature).containsKey(value)) {
					indexes.get(feature).put(value, new LinkedList<T>());
				}
				indexes.get(feature).get(value).add(annotation);
			}
		}
		long indexingTime = System.currentTimeMillis() - indexStartTime;
		logger.info("Indexing finished, took " + indexingTime + " ms.");
	}

	private void initialiseIndexes() {
		indexes = new HashMap<String, Map<String, List<T>>>();

		for (String f : featureNames) {
			indexes.put(f, new HashMap<String, List<T>>());
		}

	}
}
