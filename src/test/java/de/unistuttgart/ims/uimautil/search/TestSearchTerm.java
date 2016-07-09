package de.unistuttgart.ims.uimautil.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.uima.jcas.tcas.Annotation;
import org.junit.Test;

public class TestSearchTerm {
	SearchTerm<Annotation> term;

	@Test
	public void testPositiveExamples() throws Exception {
		term = SearchTerm.fromString("[lemma/value=\"dog\"]");
		assertNotNull(term);
		assertEquals("lemma/value", term.getFeaturePath());
		assertEquals("dog", term.getValue());

		term = SearchTerm.fromString("[lemma/value=\"dog\\\"s\"]");
		assertNotNull(term);
		assertEquals("lemma/value", term.getFeaturePath());
		assertEquals("dog\"s", term.getValue());
	}
}
