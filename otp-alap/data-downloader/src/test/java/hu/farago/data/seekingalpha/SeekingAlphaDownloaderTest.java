package hu.farago.data.seekingalpha;


import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.EarningsCall;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore("It is very time consuming")
public class SeekingAlphaDownloaderTest extends AbstractRootTest {
	
	private static final String AAPL = "AAPL";

	@Autowired
	private SeekingAlphaDownloader downloader;

	private Map<String, List<EarningsCall>> earningsCallData;
	
	@Before
	public void parseFirstPage() throws Exception {
		if (earningsCallData == null) {
			earningsCallData = downloader.parseAll(0);
		}
	}
	
	@Test
	public void testIndexes() {
		assertTrue(downloader.getIndexes().size() > 0);
	}
	
	@Test
	public void testParseAll() throws Exception {
		assertTrue(earningsCallData.get(AAPL).size() > 0);
	}
	
	@Test
	public void testWordSplit() throws Exception {
		EarningsCall firstAppleArticle = earningsCallData.get(AAPL).get(0);
		assertNotNull(firstAppleArticle.rawText);
		assertNotNull(firstAppleArticle.tradingSymbol);
		assertNotNull(firstAppleArticle.publishDate);
		assertNotNull(firstAppleArticle.words);
		assertTrue(firstAppleArticle.words.size() > 0);
	}

}
