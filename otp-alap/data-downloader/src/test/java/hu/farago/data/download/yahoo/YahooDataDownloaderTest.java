package hu.farago.data.download.yahoo;

import hu.farago.data.download.AbstractDataDowloaderTest;

import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore
public class YahooDataDownloaderTest extends AbstractDataDowloaderTest {

	@Autowired
	private YahooDataDownloader yahooDataDownloader;

	@Before
	public void setUp() throws Exception {
		dataDownloader = yahooDataDownloader;
	}
	
}
