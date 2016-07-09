package de.unistuttgart.ims.uimautil.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.Type;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.JCasIterator;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.matetools.MateLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.matetools.MatePosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;

public class TestJCasSearchIndex {
	JCas jcas;
	JCasSearchIndex<Token> index;

	@Before
	public void setUp() throws ResourceInitializationException {

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIndex() throws ClassNotFoundException, CASException, ResourceInitializationException {
		JCasIterable iterable = SimplePipeline.iteratePipeline(
				CollectionReaderFactory.createReaderDescription(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
						"src/test/resources/2.txt", TextReader.PARAM_LANGUAGE, "de"),
				AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
				AnalysisEngineFactory.createEngineDescription(MatePosTagger.class),
				AnalysisEngineFactory.createEngineDescription(MateLemmatizer.class));
		jcas = iterable.iterator().next();
		index = new JCasSearchIndex<Token>(Token.class);
		index.addIndexFeatureName("/lemma/value");
		index.index(jcas);
		Token token = JCasUtil.selectByIndex(jcas, Token.class, 0);
		FeaturePath fp = jcas.createFeaturePath();

		fp.initialize("/lemma/value");
		Type type = jcas.getTypeSystem().getType(Token.class.getName());
		fp.typeInit(type);
		assertNotNull(fp.getValueAsString(token));
		assertEquals("der", fp.getValueAsString(token));
		SearchTerm<Token> term = new SearchTerm<Token>();
		term.setFeaturePath("/lemma/value");
		term.setValue("der");

		List<Finding<Token>> findings = index.get(term);
		assertEquals(7821, findings.size());

		SearchTerm<Token> term2 = new SearchTerm<Token>();
		term2.setFeaturePath("/lemma/value");
		term2.setValue("jung");

		findings = index.get(term, term2);
		assertEquals(15, findings.size());

	}

	@Test
	public void testMultipleFiles()
			throws ResourceInitializationException, CASException, ClassNotFoundException, IOException {
		JCasIterator iterable = SimplePipeline.iteratePipeline(
				CollectionReaderFactory.createReaderDescription(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
						"src/test/resources/files/*.txt", TextReader.PARAM_LANGUAGE, "en"),
				AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
				AnalysisEngineFactory.createEngineDescription(MatePosTagger.class),
				AnalysisEngineFactory.createEngineDescription(MateLemmatizer.class)).iterator();
		index = new JCasSearchIndex<Token>(Token.class);
		index.addIndexFeatureName("/lemma/value");
		List<JCas> jcass = new LinkedList<JCas>();
		while (iterable.hasNext()) {
			jcass.add(iterable.next());
		}

		for (JCas jcas : jcass) {
			index.index(jcas);
		}

		List<Finding<Token>> f = index.get("[/lemma/value=the]");
		assertEquals(2, f.size());

		Token[] tokens = new Token[] { f.get(0).getFindings().get(0), f.get(1).getFindings().get(0) };
		assertFalse(tokens[0].getCAS() == tokens[1].getCAS());
		assertEquals("dog",
				JCasUtil.selectFollowing(Token.class, f.get(0).getFindings().get(0), 1).get(0).getCoveredText());
		assertEquals("cat",
				JCasUtil.selectFollowing(Token.class, f.get(1).getFindings().get(0), 1).get(0).getCoveredText());
	}

}
