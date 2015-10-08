package hu.farago.data.download.stooq;

import hu.farago.data.download.AbstractDataDownloaderTest;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class StooqDataDownloaderTest extends AbstractDataDownloaderTest {
	
	@Autowired
	private StooqDataDownloader stooqDataDownloader;

	@Before
	public void setUp() throws Exception {
		dataDownloader = stooqDataDownloader;
	}


}
