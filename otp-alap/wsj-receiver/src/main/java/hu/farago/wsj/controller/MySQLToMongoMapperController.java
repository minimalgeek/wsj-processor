package hu.farago.wsj.controller;

import hu.farago.wsj.api.Converter;
import hu.farago.wsj.model.dao.mongo.ArticleCollectionManager;
import hu.farago.wsj.model.dao.sql.ArticleRepository;
import hu.farago.wsj.model.dao.sql.ArticleWordRepository;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;
import hu.farago.wsj.model.entity.sql.Article;
import hu.farago.wsj.model.entity.sql.ArticleWord;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MySQLToMongoMapperController implements Converter<Article, ArticleCollection>{
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MySQLToMongoMapperController.class);
	
	private static final int PAGE_SIZE = 500;

	@Autowired
	private ArticleCollectionManager articleCollectionManager;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private ArticleWordRepository articleWordRepository;
	
	@RequestMapping(value = "/moveArticles", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Long moveArticles() {
		LOGGER.info("moveArticles");

		Iterable<Article> articles;
		long numberOfArticles = articleRepository.count();
		long numberOfPages = (long) Math.ceil(numberOfArticles/(double)PAGE_SIZE);
		
		//articleCollectionManager.deleteAll();
		
		for (int idx = 0; idx < numberOfPages; idx++) {
			LOGGER.info("processing page " + idx + "...");
			
			articles = articleRepository.findAll(new PageRequest(idx, PAGE_SIZE));
			List<ArticleCollection> collectionOfArticles = new LinkedList<ArticleCollection>();
			for (Article article : articles) {
				collectionOfArticles.add(convertTo(article));
			}
			
			articleCollectionManager.save(collectionOfArticles);
		}
		
		return numberOfArticles;
	}
	
	@Override
	public Article convertFrom(ArticleCollection obj) {
		return null;
	}
	
	@Override
	public ArticleCollection convertTo(Article sqlObject) {
		ArticleCollection article = new ArticleCollection();
		
		//article.setId(sqlObject.getId());
		article.setDateDay(new Date(sqlObject.getDateDay().getTime()));
		article.setDateTime(new Date(sqlObject.getDateTime().getTime()));
		article.setPlainText(sqlObject.getPlainText());
		article.setProcessed(sqlObject.getProcessed());
		article.setRawText(sqlObject.getRawText());
		article.setTitle(sqlObject.getTitle());
		article.setUrl(sqlObject.getUrl());
		
		for (ArticleWord word : articleWordRepository.findByArticle(sqlObject)) {
			article.getArticleWords().add(word.getWord());
		}
		
		return article;
	}
}
