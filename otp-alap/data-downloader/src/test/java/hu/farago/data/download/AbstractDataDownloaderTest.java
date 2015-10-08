package hu.farago.data.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hu.farago.data.api.ForexDataDownloader;
import hu.farago.data.api.dto.ForexData;
import hu.farago.data.config.AbstractRootTest;

import org.junit.Test;

public abstract class AbstractDataDownloaderTest extends AbstractRootTest {
	
	protected static final String USDEUR = "USDEUR";
	
	protected ForexDataDownloader dataDownloader;
	
	@Test
	public void getDataForSymbolTest() {
		ForexData data = dataDownloader.getDataForSymbol(USDEUR);
		assertNotNull(data);
		assertEquals(USDEUR, data.getSymbol());
		assertNotNull(data.getHistoricalForexDataList());
	}

	@Test
	public void getDataForSymbolBetweenDatesTest() {
		ForexData data = dataDownloader.getDataForSymbolBetweenDates(USDEUR, getNewDate(2010, 0, 1), getNewDate(2010, 1, 1));
		assertNotNull(data);
		assertEquals(USDEUR, data.getSymbol());
		assertNotNull(data.getHistoricalForexDataList());
		
		assertEquals(21, data.getHistoricalForexDataList().size());
	}

}
