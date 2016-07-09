package de.unistuttgart.ims.uimautil.search;

import org.apache.uima.jcas.tcas.Annotation;

public class FeatureValueSearchTerm<T extends Annotation> {
	String featurePath;
	String value;

	public String getFeaturePath() {
		return featurePath;
	}

	public void setFeaturePath(String featurePath) {
		this.featurePath = featurePath;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('[');
		b.append(featurePath).append("==");
		b.append('"').append(value).append('"');
		b.append(']');
		return b.toString();
	}

}
