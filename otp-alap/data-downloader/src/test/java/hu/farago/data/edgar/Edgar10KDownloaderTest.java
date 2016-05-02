package hu.farago.data.edgar;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.Edgar10QData;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

public class Edgar10KDownloaderTest extends AbstractRootTest {
	
	@Autowired
	private Edgar10KDownloader downloader; 
	
	@Test
	public void collectAllDataForIndexTest() throws IOException, SAXException, TikaException, ParseException {
		downloader.clean();
		List<Edgar10QData> ret = downloader.collectAllDataForIndex("DNR");
		Assert.assertNotNull(ret);
	}

}
