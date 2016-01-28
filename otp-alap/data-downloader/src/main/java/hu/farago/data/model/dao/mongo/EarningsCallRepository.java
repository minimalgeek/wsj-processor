package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.EarningsCall;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EarningsCallRepository extends MongoRepository<EarningsCall, BigInteger> {
	
}