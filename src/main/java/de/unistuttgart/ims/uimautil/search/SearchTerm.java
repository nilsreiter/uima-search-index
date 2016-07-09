package de.unistuttgart.ims.uimautil.search;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.uima.jcas.tcas.Annotation;

public class SearchTerm<T extends Annotation> {
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

	public static <T extends Annotation> SearchTerm<T> fromString(String s) throws Exception {
		StringTokenizer st = new StringTokenizer(s, "[]=", true);
		if (st.countTokens() != 5)
			throw new Exception(s + " can not be parsed.");
		SearchTerm<T> term = new SearchTerm<T>();
		int i = 0;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (i == 1)
				term.setFeaturePath(token);
			if (i == 3)
				term.setValue(StringEscapeUtils.unescapeJava(StringUtils.strip(token, "\"")));
			i++;
		}
		return term;
	}

}
