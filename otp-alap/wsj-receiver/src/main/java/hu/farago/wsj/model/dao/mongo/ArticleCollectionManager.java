package hu.farago.wsj.model.dao.mongo;

import java.math.BigInteger;

import hu.farago.wsj.model.entity.mongo.ArticleCollection;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleCollectionManager extends MongoRepository<ArticleCollection, BigInteger> {
	
	ArticleCollection findByUrl(String lastname);
	
}
