package de.unistuttgart.ims.uimautil.search;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.jcas.tcas.Annotation;

public class QueryParser {
	Map<String, String> keyMappings = new HashMap<String, String>();

	public void addKeyMapping(String key, String value) {
		keyMappings.put(key, value);
	}

	public Map<String, String> getKeyMappings() {
		return keyMappings;
	}

	public <T extends Annotation> SearchTerm[] parse(String s) throws IOException {
		StreamTokenizer st = new StreamTokenizer(new StringReader(s));
		st.slashStarComments(false);
		st.wordChars('/', '/');
		st.ordinaryChar('=');

		ArrayList<SearchTerm> ret = new ArrayList<SearchTerm>();
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
				SearchTerm term = new SearchTerm();

				String key = tokens.get(i + 1);
				if (keyMappings.containsKey(key)) {
					term.setFeaturePath(keyMappings.get(key));
				} else {
					term.setFeaturePath(key);
				}
				term.setValue(tokens.get(i + 3));
				ret.add(term);
			}
		}

		return ret.toArray(new SearchTerm[ret.size()]);

	}
}
