package hu.farago.data.edgar;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.EdgarData;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

public class EdgarDownloaderTest extends AbstractRootTest {
	
	@Autowired
	private EdgarDownloader downloader; 
	
	@Test
	public void collectAllDataForIndexTest() throws IOException, SAXException, TikaException, ParseException {
		downloader.clean();
		List<EdgarData> ret = downloader.collectAllDataForIndex("AAPL");
		Assert.assertNotNull(ret);
	}

}
