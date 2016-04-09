package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.EdgarData;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EdgarDataRepository extends MongoRepository<EdgarData, BigInteger> {
	
	List<EdgarData> findByTradingSymbol(String symbol);
	
}