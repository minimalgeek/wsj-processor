package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.InsiderDataGroup;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InsiderDataGroupRepository extends MongoRepository<InsiderDataGroup, BigInteger> {
	
	List<InsiderDataGroup> findByIssuerTradingSymbol(String symbol);
	
}