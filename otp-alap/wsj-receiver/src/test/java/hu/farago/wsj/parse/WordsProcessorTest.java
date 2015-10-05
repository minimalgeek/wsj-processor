package hu.farago.wsj.parse;

import hu.farago.wsj.config.AbstractRootTest;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;
import hu.farago.wsj.parse.wordprocess.CoreNLPWordsProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.tika.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WordsProcessorTest extends AbstractRootTest {

	private static final String SAMPLE_TXT = "words-processor-sample.txt";
	private static final String CORENLP_SAMPLE_EXPECTED_TXT = "coreNLP-words-processor-sample-expected.txt";

	@Autowired
	private CoreNLPWordsProcessor nlpBasedWordsProcessor;

	private ArticleCollection article;

	private Set<String> coreNLPWords;

	@Before
	public void setup() throws IOException {
		InputStream stream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(SAMPLE_TXT);

		article = new ArticleCollection();
		article.setPlainText(IOUtils.toString(stream, "UTF-8"));

		InputStream expectedStream = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(CORENLP_SAMPLE_EXPECTED_TXT);
		coreNLPWords = new HashSet<String>(IOUtils.readLines(expectedStream,
				"UTF-8"));
	}

	@Test
	public void testCoreNLPWordsProcessing() {
		nlpBasedWordsProcessor.parseArticlePlainTextAndBuildMapOfWords(article);
		Assert.assertThat(article.getArticleWords(),
				Matchers.containsInAnyOrder(coreNLPWords.toArray()));
	}

}
