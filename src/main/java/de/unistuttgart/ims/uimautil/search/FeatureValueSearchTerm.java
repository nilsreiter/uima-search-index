package de.unistuttgart.ims.uimautil.search;

import org.apache.uima.cas.Feature;

public class FeatureValueSearchTerm implements SearchTerm {
	Feature feature;
	String value;

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
