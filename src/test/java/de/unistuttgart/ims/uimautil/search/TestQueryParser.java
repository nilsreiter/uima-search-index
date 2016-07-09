package de.unistuttgart.ims.uimautil.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.uima.jcas.tcas.Annotation;
import org.junit.Test;

public class TestQueryParser {
	SearchTerm<Annotation>[] terms = null;

	@Test
	public void testParser() throws IOException {
		QueryParser qp = new QueryParser();

		terms = qp.parse("[lemma/value=\"bla\"]");
		assertNotNull(terms);
		assertEquals(1, terms.length);
		assertEquals("lemma/value", terms[0].getFeaturePath());
		assertEquals("bla", terms[0].getValue());

		terms = qp.parse("[lemma/value=\"bla\\\"s\"]");
		assertNotNull(terms);
		assertEquals(1, terms.length);
		assertEquals("lemma/value", terms[0].getFeaturePath());
		assertEquals("bla\"s", terms[0].getValue());

		terms = qp.parse("[lemma/value=bla]");
		assertNotNull(terms);
		assertEquals(1, terms.length);
		assertEquals("lemma/value", terms[0].getFeaturePath());
		assertEquals("bla", terms[0].getValue());

		terms = qp.parse("[lemma/value=bla][pos/posTag=NN]");
		assertNotNull(terms);
		assertEquals(2, terms.length);
		assertEquals("lemma/value", terms[0].getFeaturePath());
		assertEquals("pos/posTag", terms[1].getFeaturePath());

	}

	@Test
	public void testMappedParser() throws IOException {
		QueryParser qp = new QueryParser();
		qp.addKeyMapping("lemma", "lemma/value");
		qp.addKeyMapping("pos", "pos/posTag");

		terms = qp.parse("[lemma=\"bla\"]");
		assertNotNull(terms);
		assertEquals(1, terms.length);
		assertEquals("lemma/value", terms[0].getFeaturePath());
		assertEquals("bla", terms[0].getValue());

		terms = qp.parse("[lemma=\"bla\\\"s\"]");
		assertNotNull(terms);
		assertEquals(1, terms.length);
		assertEquals("lemma/value", terms[0].getFeaturePath());
		assertEquals("bla\"s", terms[0].getValue());

		terms = qp.parse("[lemma=bla]");
		assertNotNull(terms);
		assertEquals(1, terms.length);
		assertEquals("lemma/value", terms[0].getFeaturePath());
		assertEquals("bla", terms[0].getValue());

		terms = qp.parse("[lemma=bla][pos=NN]");
		assertNotNull(terms);
		assertEquals(2, terms.length);
		assertEquals("lemma/value", terms[0].getFeaturePath());
		assertEquals("pos/posTag", terms[1].getFeaturePath());

	}
}
