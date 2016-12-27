package hu.farago.data.model.dao.mongo;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

import hu.farago.data.model.entity.mongo.AutomaticServiceError;

public interface AutomaticServiceErrorRepository extends MongoRepository<AutomaticServiceError, BigInteger> {

}
