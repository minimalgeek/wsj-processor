package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.HarvardWords;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HarvardWordsRepository extends MongoRepository<HarvardWords, BigInteger> {
	
}