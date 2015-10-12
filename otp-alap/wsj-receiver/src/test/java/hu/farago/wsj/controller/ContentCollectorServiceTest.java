package hu.farago.wsj.controller;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import hu.farago.wsj.config.AbstractRootTest;
import hu.farago.wsj.controller.ContentCollectorService;
import hu.farago.wsj.model.dao.mongo.MetaInfoCollectionManager;
import hu.farago.wsj.model.entity.mongo.MetaInfoCollection;
import hu.farago.wsj.model.entity.mongo.MetaInfoCollection.KEYS;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentCollectorServiceTest extends AbstractRootTest {

	@Autowired
	private MetaInfoCollectionManager metaInfoCollectionManager;
	@Autowired
	private ContentCollectorService contentCollectorService;

	@Before
	public void before() {
		metaInfoCollectionManager.save(new MetaInfoCollection(KEYS.LATESTDATE,
				getNewDate(2014, 4, 30))); // 2014-05-30
	}

	@After
	public void after() {
		metaInfoCollectionManager.deleteAll();
	}

	@Test
	public void findLatestArticleDateTest() {
		DateTime dateTime = contentCollectorService.findLatestArticleDate();

		assertEquals(2014, dateTime.getYear());
		assertEquals(5, dateTime.getMonthOfYear());
		assertEquals(30, dateTime.getDayOfMonth());
	}

	@Test
	public void collectAllURLBetweenDateAndTodayTest() {
		DateTime now = DateTime.now();
		List<String> urls = contentCollectorService
				.collectAllURLBetweenDateAndToday(now.minusDays(10));

		assertEquals(11, urls.size());
		assertThat(
				urls,
				hasItems(getExpectedArchiveURL(now)));
	}
	
	@Test
	@Ignore("only for manual testing!")
	public void collectContentTest() {
		contentCollectorService.collectContent();
	}

	private String getExpectedArchiveURL(DateTime date) {
		return "http://www.wsj.com/public/page/archive-"
				+ date.toString("yyyy-MM-dd") + ".html";
	}
}
