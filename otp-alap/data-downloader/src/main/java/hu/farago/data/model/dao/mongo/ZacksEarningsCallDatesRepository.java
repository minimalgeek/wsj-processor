package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.ZacksEarningsCallDates;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ZacksEarningsCallDatesRepository extends MongoRepository<ZacksEarningsCallDates, BigInteger> {

	List<ZacksEarningsCallDates> findByTradingSymbol(String symbol);
	
}
