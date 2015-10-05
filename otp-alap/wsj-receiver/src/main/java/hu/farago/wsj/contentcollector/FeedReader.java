package hu.farago.wsj.contentcollector;

import java.awt.Desktop;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

//@Service
public class FeedReader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FeedReader.class);

	@Value("#{'${wsj.receiver.feed.URL}'.split(',')}")
	private List<String> feedURLList;

	@Value("${wsj.receiver.feed.enabled}")
	private boolean feedEnabled;

	private Set<String> openedLinks = new HashSet<String>();

	//@Scheduled(fixedDelay = 5000)
	public void readFeed() {
		if (feedEnabled) {
			for (String url : feedURLList) {
				try {
					LOGGER.info("Read feed: " + url);
					URL feedUrl = new URL(url);

					SyndFeedInput input = new SyndFeedInput();
					SyndFeed feed = input.build(new XmlReader(feedUrl));

					for (Object entry : feed.getEntries()) {
						SyndEntryImpl syndEntry = (SyndEntryImpl) entry;
						String linkToOpen = syndEntry.getLink();
						if (!openedLinks.contains(linkToOpen)) {
							Desktop.getDesktop().browse(
									java.net.URI.create(linkToOpen));
							openedLinks.add(linkToOpen);
						}
					}
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		}
	}

}
