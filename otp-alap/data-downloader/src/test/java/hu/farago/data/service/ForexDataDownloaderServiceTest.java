package hu.farago.data.service;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.service.ForexDataDownloaderService;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ForexDataDownloaderServiceTest extends AbstractRootTest {

	@Autowired
	private ForexDataDownloaderService service;

	@Test
	@Ignore("it is very time consuming")
	public void downloadAllTest() {
		service.downloadAll();
	}
	
	@Test
	public void downloadMissingTest() {
		service.downloadMissing();
	}

}
