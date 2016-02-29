package hu.farago.data.zacks;

import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.ZacksEarningsCallDatesRepository;
import hu.farago.data.model.entity.mongo.ZacksEarningsCallDates;
import hu.farago.data.zacks.ZacksECDateManager.ManagerParameterObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ZacksECDateManagerTest extends AbstractRootTest {
	
	private static final String AAPL = "AAPL";

	@Autowired
	private ZacksECDateManager manager;
	
	@Autowired
	private ZacksEarningsCallDatesRepository repository;
	
	private ManagerParameterObject sample;
	private ManagerParameterObject updatedSample;
	private ManagerParameterObject earlyReport;
	
	@Before
	public void before() {
		repository.deleteAll();
		
		sample = new ManagerParameterObject();
		sample.nextReportDate = new DateTime(2016, 1, 5, 0, 0);
		sample.tradingSymbol = AAPL;
		
		updatedSample = new ManagerParameterObject();
		updatedSample.nextReportDate = new DateTime(2016, 1, 7, 0 , 0);
		updatedSample.tradingSymbol = AAPL;
		
		earlyReport = new ManagerParameterObject();
		earlyReport.nextReportDate = new DateTime(2015, 10, 10, 0, 0);
		earlyReport.tradingSymbol = AAPL;
		
		DateTimeUtils.setCurrentMillisFixed(sample.nextReportDate.minusDays(2).getMillis());
	}

	@After
	public void after() {
		repository.deleteAll();
		DateTimeUtils.setCurrentMillisSystem();
	}


	@Test
	public void testAddDate() {
		
		manager.addDate(sample);
		manager.addDate(sample); // add the same!
		
		assertThat(repository.count(), equalTo(1L));
		
		ZacksEarningsCallDates ret = repository.findAll().get(0);
		assertThat(ret.seekingAlphaCheckDate, hasSize(3));
		assertThat(ret.seekingAlphaCheckDate, contains(new DateTime(2016, 1, 5, 0, 0),
													   new DateTime(2016, 1, 6, 0, 0),
													   new DateTime(2016, 1, 7, 0, 0)));
		
	}
	
	@Test
	public void testAddMoreDate() {
		
		manager.addDate(earlyReport);
		manager.addDate(sample);
		
		assertThat(repository.count(), equalTo(2L));
		
	}

	@Test
	public void testOverrideDate() {
		manager.addDate(sample); // current report
		manager.addDate(sample); // again :)
		manager.addDate(earlyReport); // early report
		manager.overrideDate(updatedSample); // refreshed current report
		
		assertThat(repository.count(), equalTo(2L));
		
		List<ZacksEarningsCallDates> list = repository.findAll();
		DateTime maxDate = list.stream().map(u -> u.nextReportDate).max(DateTime::compareTo).get();
		ZacksEarningsCallDates ret = Iterables.find(list, new Predicate<ZacksEarningsCallDates>() {
			@Override
			public boolean apply(ZacksEarningsCallDates input) {
				return input.nextReportDate.equals(maxDate);
			}
		});
		
		assertThat(ret.seekingAlphaCheckDate, hasSize(3));
		assertThat(ret.seekingAlphaCheckDate, contains(new DateTime(2016, 1, 7, 0, 0),
													   new DateTime(2016, 1, 8, 0, 0),
													   new DateTime(2016, 1, 9, 0, 0)));
	}

	@Test
	@Ignore
	public void testLookForTranscripts() {
		fail("Not yet implemented");
	}

}
