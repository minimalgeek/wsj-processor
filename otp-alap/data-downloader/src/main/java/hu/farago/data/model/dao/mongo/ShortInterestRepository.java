package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.ShortInterest;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShortInterestRepository extends MongoRepository<ShortInterest, BigInteger> {

	List<ShortInterest> findByTradingSymbol(String symbol);
	
}
