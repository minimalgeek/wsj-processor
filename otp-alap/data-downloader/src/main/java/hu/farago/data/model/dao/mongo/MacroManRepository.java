package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.MacroMan;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MacroManRepository extends MongoRepository<MacroMan, BigInteger> {

}