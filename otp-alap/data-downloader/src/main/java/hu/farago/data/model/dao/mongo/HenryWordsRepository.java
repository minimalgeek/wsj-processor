package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.HenryWords;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HenryWordsRepository extends MongoRepository<HenryWords, BigInteger> {
	
}