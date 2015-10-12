package hu.farago.wsj.controller;

import hu.farago.wsj.model.dao.mongo.ArticleCollectionManager;
import hu.farago.wsj.model.dao.mongo.MetaInfoCollectionManager;
import hu.farago.wsj.model.entity.mongo.MetaInfoCollection;
import hu.farago.wsj.model.entity.mongo.MetaInfoCollection.KEYS;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class ContentCollectorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentCollectorService.class);

	private static final String URL_PREFIX = "http://www.wsj.com/public/page/archive-";
	private static final String URL_POSTFIX = ".html";

	@Autowired
	private ArticleCollectionManager articleCollectionManager;
	@Autowired
	private MetaInfoCollectionManager metaInfoCollectionManager;

	@Value("${wsj.receiver.collectMissing}")
	private boolean collectMissing;

	@RequestMapping(value = "/collectContent", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> collectContent() {
		LOGGER.info("collect content");

		List<String> archiveURLList = collectAllURLBetweenDateAndToday(findLatestArticleDate());
		for (String url : archiveURLList) {
			try {
				LOGGER.info("Open url: " + url);
				URI archiveSite = new URI(url);

				Desktop.getDesktop().browse(archiveSite);
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
		}

		return archiveURLList;
	}

	@Scheduled(cron = "0 0/30 * * * ?")
	public void scheduledCollectContent() {
		if (collectMissing) {
			collectContent();
		}
	}

	public DateTime findLatestArticleDate() {
		MetaInfoCollection metaInfo = metaInfoCollectionManager
				.findByKey(KEYS.LATESTDATE.getKeyName());
		if (metaInfo == null) {
			return DateTime.now();
		}
		return new DateTime(metaInfo.getValue());
	}

	public List<String> collectAllURLBetweenDateAndToday(DateTime startDate) {
		List<String> retList = Lists.newArrayList();

		DateTime today = DateTime.now();
		do {
			StringBuilder urlBuilder = new StringBuilder(URL_PREFIX);
			urlBuilder.append(startDate.toString("yyyy-MM-dd"));
			urlBuilder.append(URL_POSTFIX);

			retList.add(urlBuilder.toString());
			startDate = startDate.plusDays(1);
		} while (startDate.isBefore(today));

		return retList;
	}
}
