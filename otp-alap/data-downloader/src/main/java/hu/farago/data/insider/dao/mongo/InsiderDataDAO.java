package hu.farago.data.insider.dao.mongo;

import hu.farago.data.insider.dto.InsiderData;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InsiderDataDAO extends MongoRepository<InsiderData, BigInteger> {
	
}