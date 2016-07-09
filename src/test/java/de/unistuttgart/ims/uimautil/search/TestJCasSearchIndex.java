package de.unistuttgart.ims.uimautil.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.Type;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.matetools.MateLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;

public class TestJCasSearchIndex {
	JCas jcas;
	JCasSearchIndex<Token> index;

	@Before
	public void setUp() throws ResourceInitializationException {
		JCasIterable iterable = SimplePipeline.iteratePipeline(
				CollectionReaderFactory.createReaderDescription(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
						"src/test/resources/1.txt", TextReader.PARAM_LANGUAGE, "de"),
				AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
				// AnalysisEngineFactory.createEngineDescription(MatePosTagger.class),
				AnalysisEngineFactory.createEngineDescription(MateLemmatizer.class));
		jcas = iterable.iterator().next();
		index = new JCasSearchIndex<Token>(Token.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIndex() throws ClassNotFoundException, CASException {
		index.index(jcas);
		Token token = JCasUtil.selectByIndex(jcas, Token.class, 0);
		FeaturePath fp = jcas.createFeaturePath();

		fp.initialize("/lemma/value");
		Type type = jcas.getTypeSystem().getType(Token.class.getName());
		fp.typeInit(type);
		assertNotNull(fp.getValueAsString(token));
		assertEquals("es", fp.getValueAsString(token));
		FeatureValueSearchTerm<Token> term = new FeatureValueSearchTerm<Token>();
		term.setFeaturePath("/lemma/value");
		term.setValue("es");
		List<Finding<Token>> findings = index.get(term);
		assertEquals(3, findings.size());
	}

}
