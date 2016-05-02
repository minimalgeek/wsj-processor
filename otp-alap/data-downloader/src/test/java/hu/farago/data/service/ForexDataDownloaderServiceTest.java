package hu.farago.data.service;

import hu.farago.data.config.AbstractRootTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ForexDataDownloaderServiceTest extends AbstractRootTest {

	@Autowired
	private ForexDataDownloaderService service;

	@Test
	public void downloadAllTest() {
		service.downloadAll();
	}
	
	@Test
	public void downloadMissingTest() {
		service.downloadMissing();
	}

}
