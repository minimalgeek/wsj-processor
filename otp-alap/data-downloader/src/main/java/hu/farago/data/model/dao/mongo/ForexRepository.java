package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.Forex;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface ForexRepository extends MongoRepository<Forex, BigInteger>, QueryDslPredicateExecutor<Forex> {

	Forex findFirstBySymbolOrderByTickDateDesc(String symbol);
	
}