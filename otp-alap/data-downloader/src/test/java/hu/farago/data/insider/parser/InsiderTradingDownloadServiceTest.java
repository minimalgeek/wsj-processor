package hu.farago.data.insider.parser;

import static org.junit.Assert.assertTrue;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.insider.service.InsiderTradingDownloadService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsiderTradingDownloadServiceTest extends AbstractRootTest {
	
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
	public void downloadAndWriteTest() throws Exception {
		downloadService.collectContent();
	}

}
