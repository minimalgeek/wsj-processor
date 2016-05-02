package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.Edgar10QData;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface Edgar10KDataRepository extends MongoRepository<Edgar10QData, BigInteger> {
	
	List<Edgar10QData> findByTradingSymbol(String symbol);
	
}