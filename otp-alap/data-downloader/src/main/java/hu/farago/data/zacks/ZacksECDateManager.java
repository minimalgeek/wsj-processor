package hu.farago.data.zacks;

import hu.farago.data.model.dao.mongo.EarningsCallRepository;
import hu.farago.data.model.dao.mongo.ZacksEarningsCallDatesRepository;
import hu.farago.data.model.entity.mongo.ZacksEarningsCallDates;
import hu.farago.data.seekingalpha.SeekingAlphaDownloader;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Component
public class ZacksECDateManager {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZacksECDateManager.class);

	@Autowired
	private ZacksEarningsCallDatesRepository zacksRepository;
	@Autowired
	private EarningsCallRepository ecRepository;
	@Autowired
	private SeekingAlphaDownloader downloader;
	@Resource
    private ThreadPoolTaskScheduler taskScheduler;
	
	public void addDate(ManagerParameterObject obj) {
		ZacksEarningsCallDates dateToStore = createMongoObjectFromParameterObject(obj);
		
		List<ZacksEarningsCallDates> listOfCallDates = zacksRepository.findByTradingSymbol(dateToStore.tradingSymbol);
		ZacksEarningsCallDates foundCallDate = Iterables.tryFind(listOfCallDates, findByDatePredicate(dateToStore.nextReportDate)).orNull();
		
		if (foundCallDate == null) {
			zacksRepository.save(dateToStore);
			LOGGER.info("Date saved: " + dateToStore.toString());
		}
	}
	
	public void overrideDate(ManagerParameterObject obj) {
		ZacksEarningsCallDates dateToStore = createMongoObjectFromParameterObject(obj);
		
		List<ZacksEarningsCallDates> listOfCallDates = zacksRepository.findByTradingSymbol(dateToStore.tradingSymbol);
		
		if (!CollectionUtils.isEmpty(listOfCallDates)) {
			DateTime maxDate = listOfCallDates.stream().map(u -> u.nextReportDate).max(DateTime::compareTo).get();
			ZacksEarningsCallDates foundCallDate = Iterables.tryFind(listOfCallDates, findByDatePredicate(maxDate)).orNull();
			
			if (foundCallDate != null && foundCallDate.nextReportDate.isAfter(DateTime.now())) {
				zacksRepository.delete(foundCallDate);
				LOGGER.info("Report date changed: " + foundCallDate.nextReportDate.toString() + 
							" to " + dateToStore.nextReportDate.toString());
			}
		}
		
		zacksRepository.save(dateToStore);
	}

	public void lookForTranscripts() {
		List<DateTime> dateTimesToCheck = Lists.newArrayList();
		DateTime today = DateTime.now().withTimeAtStartOfDay();
		
		dateTimesToCheck.add(today);
		dateTimesToCheck.add(today.plusDays(1));
		dateTimesToCheck.add(today.plusDays(2));
		
		List<ZacksEarningsCallDates> listOfCallDates = zacksRepository.findBySeekingAlphaCheckDateIn(dateTimesToCheck);
		
		for (ZacksEarningsCallDates zecd : listOfCallDates) {
			if (zecd.foundEarningsCallId == null) {
				for(DateTime dtToSchedule : zecd.seekingAlphaCheckDate) {
					taskScheduler.schedule(new ScheduledSeekingAlphaCheck(downloader, zecd, zacksRepository, ecRepository), 
							dtToSchedule.toDate());
				}
			}
		}
	}
	
	// private functions
	
	private ZacksEarningsCallDates createMongoObjectFromParameterObject(
			ManagerParameterObject obj) {
		ZacksEarningsCallDates dateToStore = new ZacksEarningsCallDates();
		
		dateToStore.nextReportDate = obj.nextReportDate.withZoneRetainFields(DateTimeZone.UTC);
		dateToStore.seekingAlphaCheckDate.add(obj.nextReportDate);
		dateToStore.seekingAlphaCheckDate.add(obj.nextReportDate.plusDays(1));
		dateToStore.seekingAlphaCheckDate.add(obj.nextReportDate.plusDays(2));
		dateToStore.tradingSymbol = obj.tradingSymbol;
		
		return dateToStore;
	}
	
	private Predicate<ZacksEarningsCallDates> findByDatePredicate(
			DateTime dt) {
		return new Predicate<ZacksEarningsCallDates>() {
			@Override
			public boolean apply(ZacksEarningsCallDates input) {
				return input.nextReportDate.equals(dt);
			}
		};
	}

	// static objects
	
	public static class ManagerParameterObject {
		public String tradingSymbol;
		public DateTime nextReportDate;
	}
}
