package hu.farago.data.yahoo.download;

import static org.junit.Assert.*;
import hu.farago.data.config.AbstractRootTest;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import yahoofinance.histquotes.HistoricalQuote;

public class YahooDataDownloaderTest extends AbstractRootTest {

	private static final String USDEUR = "USDEUR";
	
	@Autowired
	private YahooDataDownloader dataDownloader;
	
	@Test
	public void singleCurrencyHistoricalDataTest() throws IOException {
		
		Calendar fromDate = Calendar.getInstance();
		fromDate.set(2015, 0, 1);
		Calendar toDate = Calendar.getInstance();
		toDate.set(2015, 0, 3);
		
		List<HistoricalQuote> data = dataDownloader.downloadHistoricalDailyDataForCurrencyPair(USDEUR, fromDate, toDate);
		assertNotNull(data);
		assertEquals(3, data.size());
	}

}
