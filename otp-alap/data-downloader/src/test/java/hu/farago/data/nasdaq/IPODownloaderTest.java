package hu.farago.data.nasdaq;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.IPOActivity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IPODownloaderTest extends AbstractRootTest {
	
	@Autowired
	private IPODownloader downloader;
	
	@Test
	public void downloadAllActivityInMonthTest() throws Exception {
		List<IPOActivity> activities = downloader.downloadAllActivityInMonth("2016-04");
		
		Assert.assertNotNull(activities);
		Assert.assertEquals(9, activities.size());
	}
	
	@Test
	public void downloadAllActivityTest() throws Exception {
		List<IPOActivity> activities = downloader.downloadAllActivity();
		
		Assert.assertNotNull(activities);
		Assert.assertTrue(activities.size() > 100); // randomly choosen big value
	}

}
