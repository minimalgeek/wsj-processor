package hu.farago.data.nasdaq;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.ShortInterest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShortInterestDownloaderTest extends AbstractRootTest {

	@Autowired
	private ShortInterestDownloader downloader;
	
	@Test
	public void downloadShortInterestsForTradingSymbolTest() throws Exception {
		List<ShortInterest> interests = downloader.downloadShortInterestsForTradingSymbol("AAPL");
		
		Assert.assertNotNull(interests);
		Assert.assertEquals(24, interests.size());
	}
	
}
