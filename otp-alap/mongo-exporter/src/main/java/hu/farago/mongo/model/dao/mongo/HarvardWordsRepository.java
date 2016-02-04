package hu.farago.mongo.model.dao.mongo;

import hu.farago.mongo.model.entity.mongo.HarvardWords;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HarvardWordsRepository extends MongoRepository<HarvardWords, BigInteger> {
	
}