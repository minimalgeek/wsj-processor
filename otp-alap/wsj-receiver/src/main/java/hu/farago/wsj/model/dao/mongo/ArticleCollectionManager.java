package hu.farago.wsj.model.dao.mongo;

import hu.farago.wsj.model.entity.mongo.ArticleCollection;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleCollectionManager extends MongoRepository<ArticleCollection, Long> {
	
	ArticleCollection findByUrl(String lastname);
	
}
