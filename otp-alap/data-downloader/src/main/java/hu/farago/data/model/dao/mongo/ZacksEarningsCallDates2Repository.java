package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.ZacksEarningsCallDates2;

import java.math.BigInteger;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ZacksEarningsCallDates2Repository extends MongoRepository<ZacksEarningsCallDates2, BigInteger> {

	List<ZacksEarningsCallDates2> findByTradingSymbol(String symbol);
	
	ZacksEarningsCallDates2 findByTradingSymbolAndNextReportDate(String symbol, DateTime nextReportDate);
	
	//List<ZacksEarningsCallDates2> findBySeekingAlphaCheckDateIn(List<DateTime> dates);
	
}
