package hu.farago.data.zacks.service;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.zacks.service.dto.ZacksData;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

public class DataSynchronizerServiceTest extends AbstractRootTest {

	@Autowired
	private DataSynchronizerService dataSync;
	
	@Value("${zacks.url.test}")
	private String testURL;
	
	@Value("${zacks.path}")
	private String pathToCSVs;
	
	private static final String SAMPLE_RESPONSE_PATH = "sample_response.txt";
	private String sampleResponse;
	
	@Before
	public void setUp() throws Exception {
		
		InputStream stream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(SAMPLE_RESPONSE_PATH);
		
		sampleResponse = IOUtils.toString(stream, "UTF-8");
	}
	
	@Test
	public void getContentForURLTest() throws IOException, SAXException, TikaException {
		String content = dataSync.getContentForURL(testURL);
		Assert.assertNotNull(content);
		Assert.assertFalse(StringUtils.isEmpty(content));
	}
	
	@Test
	public void createZacksDataFromContentTest() throws IOException {
		ZacksData zacksData = dataSync.createZacksDataFromContent(sampleResponse);
		Assert.assertEquals(zacksData.getTitle(), "ZACKS Portfolio");
	}
	
	@Test
	public void refreshAllReportDatesTest() throws IOException {
		dataSync.refreshAllReportDates();
	}
}
