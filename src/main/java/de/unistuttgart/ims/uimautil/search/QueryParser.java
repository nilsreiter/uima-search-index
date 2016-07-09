package de.unistuttgart.ims.uimautil.search;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.jcas.tcas.Annotation;

public class QueryParser {
	public <T extends Annotation> SearchTerm<T>[] parse(String s) throws IOException {
		StreamTokenizer st = new StreamTokenizer(new StringReader(s));
		st.slashStarComments(false);
		// st.quoteChar('"');
		st.wordChars('/', '/');
		st.ordinaryChar('=');

		ArrayList<SearchTerm<T>> ret = new ArrayList<SearchTerm<T>>();
		List<String> tokens = new LinkedList<String>();
		while (st.ttype != StreamTokenizer.TT_EOF) {
			switch (st.ttype) {
			case StreamTokenizer.TT_WORD:
				tokens.add(st.sval);
				break;
			case '"':
				tokens.add(st.sval);
				break;
			case '[':
				tokens.add(String.valueOf((char) st.ttype));
				break;
			case ']':
				tokens.add(String.valueOf((char) st.ttype));
				break;
			default:
				tokens.add(String.valueOf((char) st.ttype));

			}
			st.nextToken();

		}
		System.err.println(tokens);

		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i);
			if (token.equalsIgnoreCase("[") && tokens.get(i + 4).equalsIgnoreCase("]")) {
				SearchTerm<T> term = new SearchTerm<T>();
				term.setFeaturePath(tokens.get(i + 1));
				term.setValue(tokens.get(i + 3));
				ret.add(term);
			}
		}

		return ret.toArray(new SearchTerm[ret.size()]);

	}
}
