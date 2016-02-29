package hu.farago.data.zacks;

import java.util.List;

import hu.farago.data.model.dao.mongo.ZacksEarningsCallDatesRepository;
import hu.farago.data.model.entity.mongo.ZacksEarningsCallDates;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Component
public class ZacksECDateManager {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZacksECDateManager.class);

	@Autowired
	private ZacksEarningsCallDatesRepository zacksRepository;
	
	public void addDate(ManagerParameterObject obj) {
		ZacksEarningsCallDates dateToStore = createMongoObjectFromParameterObject(obj);
		
		List<ZacksEarningsCallDates> listOfCallDates = zacksRepository.findByTradingSymbol(dateToStore.tradingSymbol);
		ZacksEarningsCallDates foundCallDate = Iterables.tryFind(listOfCallDates, findByDatePredicate(dateToStore)).orNull();
		
		if (foundCallDate == null) {
			zacksRepository.save(dateToStore);
		}
	}
	
	public void overrideDate(ManagerParameterObject obj) {
		ZacksEarningsCallDates dateToStore = createMongoObjectFromParameterObject(obj);
		
		List<ZacksEarningsCallDates> listOfCallDates = zacksRepository.findByTradingSymbol(dateToStore.tradingSymbol);
		ZacksEarningsCallDates foundCallDate = Iterables.find(listOfCallDates, findByDatePredicate(dateToStore));
		
		zacksRepository.save(dateToStore);
	}

	private Predicate<ZacksEarningsCallDates> findByDatePredicate(
			ZacksEarningsCallDates dateToStore) {
		return new Predicate<ZacksEarningsCallDates>() {

			@Override
			public boolean apply(ZacksEarningsCallDates input) {
				return input.nextReportDate.equals(dateToStore.nextReportDate);
			}
			
		};
	}
	
	public void lookForTranscripts() {
		
	}
	
	// private functions
	
	private ZacksEarningsCallDates createMongoObjectFromParameterObject(
			ManagerParameterObject obj) {
		ZacksEarningsCallDates dateToStore = new ZacksEarningsCallDates();
		
		dateToStore.nextReportDate = obj.nextReportDate;
		dateToStore.seekingAlphaCheckDate.add(obj.nextReportDate);
		dateToStore.seekingAlphaCheckDate.add(obj.nextReportDate.plusDays(1));
		dateToStore.seekingAlphaCheckDate.add(obj.nextReportDate.plusDays(2));
		dateToStore.tradingSymbol = obj.tradingSymbol;
		
		return dateToStore;
	}

	// static objects
	
	public static class ManagerParameterObject {
		public String tradingSymbol;
		public DateTime nextReportDate;
	}
}
