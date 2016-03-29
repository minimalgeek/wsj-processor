package hu.farago.data.sandp;

import java.io.IOException;
import java.util.List;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.SAndPIndex;

import org.apache.tika.exception.TikaException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

public class SAndPDownloaderTest extends AbstractRootTest {

	@Autowired
	private SAndPDownloader downloader;

	@Test
	public void testIndexes() throws IOException, SAXException, TikaException {
		List<SAndPIndex> ret = downloader.processFirstPage();
		Assert.assertNotNull(ret);
	}

}