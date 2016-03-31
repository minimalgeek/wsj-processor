package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.SAndPIndex;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SAndPIndexRepository extends MongoRepository<SAndPIndex, BigInteger> {

	SAndPIndex findByTradingSymbol(String symbol);
	
}
