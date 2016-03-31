package hu.farago.data.sandp;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.SAndPIndex;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rometools.rome.io.FeedException;

//public class SAndPRSSDownloaderTest extends AbstractRootTest {
//
//	@Autowired
//	private SAndPRSSDownloader downloader;
//
//	@Test
//	public void testIndexes() throws FeedException, IllegalArgumentException, IOException {
//		List<SAndPIndex> ret = downloader.downloadAll();
//		Assert.assertNotNull(ret);
//	}
//
//}