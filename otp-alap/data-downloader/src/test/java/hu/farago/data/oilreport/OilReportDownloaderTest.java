package hu.farago.data.oilreport;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.OilReportRepository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OilReportDownloaderTest extends AbstractRootTest {

	@Autowired
	private OilReportDownloader downloader;
	
	@Autowired
	private OilReportRepository repo;
	
	@Test
	public void downloadAndSaveAllTest() throws Exception {
		downloader.downloadAndSaveAll();

		Assert.assertTrue(repo.count() > 0);
	}

}
