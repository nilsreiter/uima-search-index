package de.unistuttgart.ims.uimautil.search;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.SimplePipeline;
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
		JCasIterable iterable = SimplePipeline.iteratePipeline(
				CollectionReaderFactory.createReaderDescription(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
						"src/test/resources/2.txt", TextReader.PARAM_LANGUAGE, "de"),
				AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
				AnalysisEngineFactory.createEngineDescription(MatePosTagger.class),
				AnalysisEngineFactory.createEngineDescription(MateLemmatizer.class));
		jcas = iterable.iterator().next();
		index = new JCasSearchIndex<Token>(Token.class);
	}

	@Test
	public void testIndex() throws ClassNotFoundException, CASException {
		index.index(jcas);
		FeatureValueSearchTerm<Token> term = new FeatureValueSearchTerm<Token>();
		term.setFeaturePath("/Lemma/Value");
		term.setValue("der");
		List<Finding> findings = index.get(term);
		assertEquals(13000, findings.size());
	}

}
