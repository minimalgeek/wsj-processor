package hu.farago.data.nasdaq;

import hu.farago.data.config.AbstractRootTest;

import java.io.IOException;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

public class CompanyListDownloaderTest extends AbstractRootTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CompanyListDownloaderTest.class);
	
	@Autowired
	private CompanyListDownloader downloader;
	
	@Test
	public void downloadNasdaqCompanyListTest() throws IOException, SAXException, TikaException {
		List<String> companies = downloader.downloadExchangeCompanyList(Exchange.NASDAQ);
		runAsserts(companies);
	}

	@Test
	public void downloadNYSECompanyListTest() throws IOException, SAXException, TikaException {
		List<String> companies = downloader.downloadExchangeCompanyList(Exchange.NYSE);
		runAsserts(companies);
	}
	
	@Test
	public void downloadAMEXCompanyListTest() throws IOException, SAXException, TikaException {
		List<String> companies = downloader.downloadExchangeCompanyList(Exchange.AMEX);
		runAsserts(companies);
	}
	
	private void runAsserts(List<String> companies) {
		Assert.assertNotNull(companies);
		Assert.assertNotEquals(0, companies.size());
		
		LOGGER.info(companies.toString());
	}
}
