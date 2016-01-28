package hu.farago.data.yahoo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hu.farago.data.api.dto.ForexData;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.yahoo.YahooCurrencyPairDownloader;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class YahooCurrencyPairDownloaderTest extends AbstractRootTest {

	@Autowired
	private YahooCurrencyPairDownloader yahooDataDownloader;
	
	@Value("${currency.pairs}")
	private String currency;

	@Test
	public void getTickDataForSymbolTest() {
		ForexData data = yahooDataDownloader.getTickDataForSymbol(currency);
		assertNotNull(data);
		assertEquals(data.getSymbol(), currency);
		assertNotNull(data.getHistoricalForexDataList());
		assertEquals(1, data.getHistoricalForexDataList().size());
	}
	
}
