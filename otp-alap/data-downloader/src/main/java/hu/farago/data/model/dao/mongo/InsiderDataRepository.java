package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.InsiderData;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InsiderDataRepository extends MongoRepository<InsiderData, BigInteger> {
	
}