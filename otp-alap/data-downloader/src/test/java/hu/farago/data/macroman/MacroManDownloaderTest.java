package hu.farago.data.macroman;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.MacroManRepository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MacroManDownloaderTest extends AbstractRootTest {

	@Autowired
	private MacroManDownloader downloader;
	
	@Autowired
	private MacroManRepository repo;
	
	@Test
	public void testDownloadAllPostsForYear() throws Exception {
		downloader.downloadAndSaveAll();
		Assert.assertTrue(repo.count() > 20); // 2015.01 - 2015.02, it should be 26, but sometimes the URLs are not available
	}

}
