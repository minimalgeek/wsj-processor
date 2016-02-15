package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.EarningsCall;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EarningsCallRepository extends MongoRepository<EarningsCall, BigInteger> {
	
	List<EarningsCall> findByTradingSymbol(String tradingSymbol);
	
}