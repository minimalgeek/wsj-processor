package hu.farago.wsj.rssfeed;

import hu.farago.wsj.config.AbstractRootTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FeedReaderTest extends AbstractRootTest {
	
	@Autowired
	private FeedReader feedReader;

	@Test
	public void testReadFeed() {
		feedReader.readFeed();
	}

}
