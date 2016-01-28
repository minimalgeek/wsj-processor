package hu.farago.data.service;

import hu.farago.data.config.AbstractRootTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsiderTradingDownloadServiceTest extends AbstractRootTest {
		
	@Autowired
	private InsiderTradingDownloadService downloadService;
	
	@Test
	public void downloadAndWriteTest() throws Exception {
		downloadService.collectContent();
	}

}
