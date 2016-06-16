package hu.farago.data.oilreport;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.OilReport;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OilReportDownloaderTest extends AbstractRootTest {

	@Autowired
	private OilReportDownloader downloader;
	
	@Test
	public void downloadAllForYearTest() throws Exception {
		List<OilReport> ret = downloader.downloadAllForYear(2015);
		Assert.assertEquals(12, ret.size());
	}

}
