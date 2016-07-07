package de.unistuttgart.ims.uimautil.search;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;

public class TestJCasSearchIndex {
	JCas jcas;
	JCasSearchIndex index;

	@Before
	public void setUp() throws ResourceInitializationException {
		JCasIterable iterable = SimplePipeline.iteratePipeline(
				CollectionReaderFactory.createReaderDescription(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
						"src/test/resources/war-and-peace.txt", TextReader.PARAM_LANGUAGE, "en"),
				AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
				AnalysisEngineFactory.createEngineDescription(StanfordPosTagger.class),
				AnalysisEngineFactory.createEngineDescription(StanfordLemmatizer.class));
		jcas = iterable.iterator().next();
		index = new JCasSearchIndex(jcas.getTypeSystem());
	}

	@Test
	public void testIndex() throws ClassNotFoundException {
		index.index(jcas);
		FeatureValueSearchTerm term = new FeatureValueSearchTerm();
		term.setFeature(jcas.getTypeSystem()
				.getFeatureByFullName("de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma:Value"));
		term.setValue("the");
		List<Finding> findings = index.get(term);
		assertEquals(13000, findings.size());
	}

}
