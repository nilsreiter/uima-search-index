package de.unistuttgart.ims.uimautil.search;

import org.apache.uima.jcas.tcas.Annotation;

public class FeatureValueSearchTerm<T extends Annotation> {
	String featurePath;

	public String getFeaturePath() {
		return featurePath;
	}

	public void setFeaturePath(String featurePath) {
		this.featurePath = featurePath;
	}

	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
