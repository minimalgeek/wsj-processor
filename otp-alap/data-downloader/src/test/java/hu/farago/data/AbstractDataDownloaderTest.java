package hu.farago.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hu.farago.data.api.ForexDataDownloader;
import hu.farago.data.api.dto.ForexData;
import hu.farago.data.config.AbstractRootTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractDataDownloaderTest extends AbstractRootTest {
	
	@Value("${currency.pairs}")
	private String pair;
	
	protected ForexDataDownloader dataDownloader;
	
	@Test
	public void getDataForSymbolTest() {
		ForexData data = dataDownloader.getDataForSymbol(pair);
		assertNotNull(data);
		assertEquals(pair, data.getSymbol());
		assertNotNull(data.getHistoricalForexDataList());
	}

	@Test
	public void getDataForSymbolBetweenDatesTest() {
		ForexData data = dataDownloader.getDataForSymbolBetweenDates(pair, getNewDate(2010, 1, 1), getNewDate(2010, 2, 1));
		assertNotNull(data);
		assertEquals(pair, data.getSymbol());
		assertNotNull(data.getHistoricalForexDataList());
		
		assertEquals(21, data.getHistoricalForexDataList().size());
	}

}
