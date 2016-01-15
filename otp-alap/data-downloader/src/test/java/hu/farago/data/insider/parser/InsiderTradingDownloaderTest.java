package hu.farago.data.insider.parser;

import static org.junit.Assert.*;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.insider.dto.InsiderData;
import hu.farago.data.insider.service.InsiderTradingDownloadService;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsiderTradingDownloaderTest extends AbstractRootTest {
	
	@Autowired
	private InsiderTradingDownloader insiderTradingParser;
	@Autowired
	private InsiderTradingDownloadService downloadService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testIndexes() {
		assertTrue(InsiderTradingDownloader.INDEXES.size() > 0);
	}
	
	@Test
	public void parseAllTest() throws Exception {
		Map<String, List<InsiderData>> insiderMap = insiderTradingParser.parseAll();
		assertEquals(InsiderTradingDownloader.INDEXES.size(), insiderMap.size());
	}
	
	@Test
	public void downloadAndWriteTest() throws Exception {
		downloadService.collectContent();
	}

}
