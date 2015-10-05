package hu.farago.wsj.model.dao.mongo;

import hu.farago.wsj.model.entity.mongo.MetaInfoCollection;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MetaInfoCollectionManager extends MongoRepository<MetaInfoCollection, BigInteger> {
	
	MetaInfoCollection findByKey(String key);
	
}
