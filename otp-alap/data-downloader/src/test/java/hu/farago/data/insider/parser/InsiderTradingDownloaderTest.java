package hu.farago.data.insider.parser;

import static org.junit.Assert.*;
import hu.farago.data.config.AbstractRootTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsiderTradingDownloaderTest extends AbstractRootTest {
	
	@Autowired
	private InsiderTradingDownloader insiderTradingParser;

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

}
