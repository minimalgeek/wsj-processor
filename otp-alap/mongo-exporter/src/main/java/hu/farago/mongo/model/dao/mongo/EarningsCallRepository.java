package hu.farago.mongo.model.dao.mongo;

import hu.farago.mongo.model.entity.mongo.EarningsCall;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EarningsCallRepository extends MongoRepository<EarningsCall, BigInteger> {
	
}