package hu.farago.yahoo.download;

import hu.farago.yahoo.dto.TickData;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.hamcrest.core.IsAnything;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.GreaterThan;

public class YahooCurrencPairDownloaderTest {
	
	private YahooCurrencPairDownloader downloader;

	@Before
	public void setUp() throws Exception {
		downloader = new YahooCurrencPairDownloader();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTickDataForCurrencyPair() throws IOException {
		TickData response = downloader.getTickDataForCurrencyPair("USDEUR");
		Assert.assertNotNull(response);
		Assert.assertThat(response.getHigh(), new GreaterThan<Double>(0.0));
		Assert.assertThat(response.getLow(), new GreaterThan<Double>(0.0));
		Assert.assertThat(response.getOpen(), new GreaterThan<Double>(0.0));
		Assert.assertThat(response.getTime(), new IsAnything<Date>());
		Assert.assertThat(response.getVolume(), new GreaterOrEqual<Long>(0L));
		Assert.assertThat(response.getLast(), new GreaterThan<Double>(0.0));
		Assert.assertNotNull(response.getCurrencyPair());
	}
	
	@Test
	public void testGetAllTickData() throws IOException {
		List<TickData> response = downloader.getAllTickData();
		Assert.assertThat(response.size(), new GreaterThan<Integer>(0));
	}

}
