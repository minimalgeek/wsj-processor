package hu.farago.data.stooq;

import hu.farago.data.AbstractDataDownloaderTest;
import hu.farago.data.stooq.StooqDataDownloader;

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
