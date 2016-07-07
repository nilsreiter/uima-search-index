package de.unistuttgart.ims.uimautil.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class JCasSearchIndex {

	TypeSystem typeSystem;

	Map<Feature, Map<String, Collection<Annotation>>> indexes = new HashMap<Feature, Map<String, Collection<Annotation>>>();

	String[] featureNames = new String[] { "de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS:PosValue",
			"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma:Value" };

	public JCasSearchIndex(TypeSystem typeSystem) {
		this.typeSystem = typeSystem;
		for (String f : featureNames) {
			Feature feature = typeSystem.getFeatureByFullName(f);
			indexes.put(feature, new HashMap<String, Collection<Annotation>>());
		}
	}

	public List<Finding> get(FeatureValueSearchTerm... terms) throws ClassNotFoundException {
		if (terms.length == 0)
			return Collections.emptyList();

		List<Finding> ret = new LinkedList<Finding>();
		Finding currentFinding;
		for (Annotation anno : indexes.get(terms[0].getFeature()).get(terms[0].getValue())) {
			currentFinding = new Finding(anno);
			Annotation nextAnnotation = anno;

			for (int i = 1; i < terms.length; i++) {
				@SuppressWarnings("unchecked")
				Class<? extends Annotation> nextClass = (Class<? extends Annotation>) Class
						.forName(terms[i].getFeature().getDomain().getName());
				nextAnnotation = JCasUtil.selectFollowing(nextClass, nextAnnotation, 1).get(0);
				if (nextAnnotation.getFeatureValueAsString(terms[i].getFeature()).equals(terms[i].getValue())) {
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

	public void index(JCas jcas) throws ClassNotFoundException {
		for (Feature feature : indexes.keySet()) {
			AnnotationIndex<Annotation> annoIndex = jcas.getAnnotationIndex(feature.getDomain());
			if (annoIndex == null)
				continue;
			FSIterator<Annotation> iterator = annoIndex.iterator();
			while (iterator.hasNext()) {
				Annotation annotation = iterator.next();
				String value = annotation.getFeatureValueAsString(feature);
				if (!indexes.get(feature).containsKey(value)) {
					indexes.get(feature).put(value, new HashSet<Annotation>());
				}
				indexes.get(feature).get(value).add(annotation);
			}
		}
	}
}
