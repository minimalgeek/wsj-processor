package hu.farago.data.zacks;

import java.util.List;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.ZacksEarningsCallDates2;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ZacksStockQuoteDownloaderTest extends AbstractRootTest {
	
	@Autowired
	private ZacksStockQuoteDownloader downloader;
	
	@Test
	public void downloadAllZECDTest() throws Exception {
		List<ZacksEarningsCallDates2> ret = downloader.downloadAllZECD();
		
		Assert.assertNotNull(ret);
		Assert.assertTrue(ret.size() > 5); // S&P 100, 400, 500, 600
	}

}
