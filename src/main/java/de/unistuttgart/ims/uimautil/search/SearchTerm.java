package de.unistuttgart.ims.uimautil.search;

public class SearchTerm {
	String featurePath;
	String value;
	Comparison comparison = Comparison.EQUALITY;

	public Comparison getComparison() {
		return comparison;
	}

	public void setComparison(Comparison comparison) {
		this.comparison = comparison;
	}

	public static enum Comparison {
		EQUALITY
	};

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
		b.append(featurePath).append("=");
		b.append('"').append(value).append('"');
		b.append(']');
		return b.toString();
	}

}
