package hu.farago.data.seekingalpha;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.seekingalpha.dto.StockData;

import java.util.Collection;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class YahooStockDownloaderTest extends AbstractRootTest {

	private EarningsCall sampleCall;
	
	@Autowired
	private YahooStockDownloader downloader;
	
	@Before
	public void before() {
		sampleCall = new EarningsCall();
		sampleCall.tradingSymbol = "AAPL";
		sampleCall.publishDate = new DateTime().minusYears(1);
	}

	@After
	public void after() {
		
	}

	@Test
	public void testAddStockData() {
		downloader.addStockData(sampleCall);
		assertThat((Collection<StockData>)(sampleCall.stockData), is(not(empty())));
	}

}
