package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.IPOActivity;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPOActivityRepository extends MongoRepository<IPOActivity, BigInteger> {

}
