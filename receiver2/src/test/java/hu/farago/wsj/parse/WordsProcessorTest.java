package hu.farago.wsj.parse;

import hu.farago.wsj.config.AbstractRootTest;
import hu.farago.wsj.model.entity.sql.Article;
import hu.farago.wsj.model.entity.sql.ArticleWord;
import hu.farago.wsj.parse.wordprocess.WordsProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.xml.sax.SAXException;

public class WordsProcessorTest extends AbstractRootTest {
	
	private static final String SAMPLE_TXT = "words-processor-sample.txt";
	private static final String SAMPLE_EXPECTED_TXT = "words-processor-sample-expected.txt";
	private static final String CORENLP_SAMPLE_EXPECTED_TXT = "coreNLP-words-processor-sample-expected.txt";
	
	
	@Autowired
	@Qualifier("regex")
	private WordsProcessor wordsProcessor;
	
	@Autowired
	@Qualifier("coreNLP")
	private WordsProcessor nlpBasedWordsProcessor;
	
	private Article article;
	
	private Set<String> words;
	
	private Set<String> coreNLPWords;
	
	@Before
	public void setup() throws IOException {
		InputStream stream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(SAMPLE_TXT);
		
		article = new Article();
		article.setPlainText(IOUtils.toString(stream, "UTF-8"));
		
		InputStream expectedStream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(SAMPLE_EXPECTED_TXT);
		words = new HashSet<String>(IOUtils.readLines(expectedStream, "UTF-8"));
		
		expectedStream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(CORENLP_SAMPLE_EXPECTED_TXT);
		coreNLPWords = new HashSet<String>(IOUtils.readLines(expectedStream, "UTF-8"));
	}
	
	@Test
	public void testWordsProcessing() throws IOException, SAXException, TikaException {
		wordsProcessor.parseArticlePlainTextAndBuildMapOfWords(article);
		Set<String> resultWords = addWordsToSet();
		Assert.assertThat(resultWords, Matchers.containsInAnyOrder(words.toArray()));
	}
	
	@Test
	public void testCoreNLPWordsProcessing() throws IOException, SAXException, TikaException {
		nlpBasedWordsProcessor.parseArticlePlainTextAndBuildMapOfWords(article);
		Set<String> resultWords = addWordsToSet();
		Assert.assertThat(resultWords, Matchers.containsInAnyOrder(coreNLPWords.toArray()));
	}

	private Set<String> addWordsToSet() {
		Set<String> resultWords = new HashSet<String>();
		for (ArticleWord articleWord : article.getArticleWords()) {
			resultWords.add(articleWord.getWord());
		}
		return resultWords;
	}

}
