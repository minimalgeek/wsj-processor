package hu.farago.wsj.parse;

import hu.farago.wsj.config.AbstractRootTest;
import hu.farago.wsj.model.entity.Article;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

public class ArticleParserTest extends AbstractRootTest {
	
	private static final String PARSER_SAMPLE_HTML = "parser-sample.html";
	private static final String PARSER_SAMPLE_EXPECTED_TXT = "parser-sample-expected.txt";
	
	@Autowired
	private ArticleParser articleParser;
	
	private Article article;
	
	private String expectedSampleText;

	@Before
	public void setUp() throws Exception {
		
		InputStream stream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(PARSER_SAMPLE_HTML);
		
		InputStream expectedStream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(PARSER_SAMPLE_EXPECTED_TXT);
		expectedSampleText = IOUtils.toString(expectedStream, "UTF-8");
		
		article = new Article();
		article.setTitle("Parse title");
		article.setUrl("http://parse.url");
		article.setRawText(IOUtils.toString(stream, "UTF-8"));
	}

	@Test
	public void testParseArticle() throws IOException, SAXException, TikaException {
		articleParser.parseArticleAndSetPlainText(article);
		Assert.assertEquals(article.getPlainText(), expectedSampleText);
	}

}
