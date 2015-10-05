package hu.farago.wsj.controller;

import hu.farago.wsj.api.Converter;
import hu.farago.wsj.controller.dto.ArticleDTO;
import hu.farago.wsj.model.dao.mongo.ArticleCollectionManager;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;
import hu.farago.wsj.parse.ArticleParser;
import hu.farago.wsj.parse.wordprocess.CoreNLPWordsProcessor;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentReceiverController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentReceiverController.class);

	@Autowired
	private ArticleCollectionManager articleCollectionManager;
	@Autowired
	@Qualifier("mongoConverter")
	private Converter<ArticleCollection, ArticleDTO> converter;

	@Autowired
	private ArticleParser articleParser;
	@Autowired
	private CoreNLPWordsProcessor wordsProcessor;

	@RequestMapping(value = "/save-article", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void saveArticle(@Valid @RequestBody ArticleDTO article) {
		LOGGER.info("received article: " + article.toString());

		ArticleCollection articleCollection = converter.convertFrom(article);
		ArticleCollection existingArticleEntity = articleCollectionManager
				.findByUrl(articleCollection.getUrl());

		if (existingArticleEntity == null) {
			LOGGER.info("saving article");
			existingArticleEntity = articleCollection;
		} else {
			LOGGER.info("updating article");
			existingArticleEntity.setTitle(articleCollection.getTitle());
			existingArticleEntity.setRawText(articleCollection.getRawText());
			existingArticleEntity.setDateTime(articleCollection.getDateTime());
		}

		try {
			articleParser.parseArticleAndSetPlainText(existingArticleEntity);
			wordsProcessor.parseArticlePlainTextAndBuildMapOfWords(existingArticleEntity);
			existingArticleEntity.setProcessed(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			existingArticleEntity.setProcessed(false);
		}

		articleCollectionManager.save(existingArticleEntity);
	}

}