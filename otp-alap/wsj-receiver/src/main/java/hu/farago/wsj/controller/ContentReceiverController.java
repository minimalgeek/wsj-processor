package hu.farago.wsj.controller;

import hu.farago.wsj.api.Converter;
import hu.farago.wsj.controller.dto.ArticleDTO;
import hu.farago.wsj.model.dao.sql.ArticleRepository;
import hu.farago.wsj.model.dao.sql.ArticleWordRepository;
import hu.farago.wsj.model.entity.sql.Article;
import hu.farago.wsj.parse.ArticleParser;
import hu.farago.wsj.parse.wordprocess.WordsProcessor;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentReceiverController implements
		Converter<Article, ArticleDTO> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentReceiverController.class);
	
	private static final int PAGE_SIZE = 500;

	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private ArticleWordRepository articleWordRepositroy;
	@Autowired
	private ArticleParser articleParser;
	@Autowired
	@Qualifier("coreNLP")
	private WordsProcessor wordsProcessor;
	@Value("${wsj.receiver.processPlainText}")
	private Boolean processPlainText;
	@Value("${wsj.receiver.processWords}")
	private Boolean processWords;

	@RequestMapping(value = "/save-article", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void saveArticle(@Valid @RequestBody ArticleDTO article) {
		LOGGER.info("received article: " + article.toString());

		Article articleEntity = convertFrom(article);
		Article existingArticleEntity = articleRepository
				.findByUrlAndFetchWords(articleEntity.getUrl());

		if (existingArticleEntity == null) {
			LOGGER.info("saving article");
			existingArticleEntity = articleRepository.save(articleEntity);
		} else {
			LOGGER.info("updating article");
			existingArticleEntity.setTitle(articleEntity.getTitle());
			existingArticleEntity.setRawText(articleEntity.getRawText());
			existingArticleEntity.setDateTime(articleEntity.getDateTime());
			existingArticleEntity = articleRepository
					.save(existingArticleEntity);
		}

		if (processPlainText) {
			processPlainText(existingArticleEntity);
		}
		
		if (processWords) {
			processWords(existingArticleEntity);
		}
	}

	private boolean processPlainText(Article existingArticleEntity) {
		try {
			LOGGER.info("parse raw text");
			articleParser
					.parseArticleAndSetPlainText(existingArticleEntity);
			articleRepository.save(existingArticleEntity);
			return true;
		} catch (Exception e) {
			LOGGER.error("exception happened during article content parsing: "
					+ existingArticleEntity.getUrl());
			LOGGER.debug(e.getMessage(), e);
			return false;
		}
	}
	
	private boolean processWords(Article existingArticleEntity) {
		try {
			LOGGER.info("process words");
			wordsProcessor
					.parseArticlePlainTextAndBuildMapOfWords(existingArticleEntity);
			articleRepository.save(existingArticleEntity);
			return true;
		} catch (Exception e) {
			LOGGER.error("exception happened during article words processing: "
					+ existingArticleEntity.getUrl());
			LOGGER.debug(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * Process all article's plain text, and returns with the number of successfully processed articles
	 * @return number of processed articles
	 */
	@RequestMapping(value = "/processAllArticle", method = RequestMethod.GET)
	public Long processAllArticle() {
		
		LOGGER.info("process all articles");
		
		Iterable<Article> articles;
		long numberOfArticles = articleRepository.countAllUnProcessed();
		long numberOfPages = (long) Math.ceil(numberOfArticles/(double)PAGE_SIZE);
		
		for (int idx = 0; idx < numberOfPages; idx++) {
			LOGGER.info("Processing page " + idx + "...");
			articles = articleRepository.findAllUnProcessed(new PageRequest(idx, PAGE_SIZE));
			for (Article article : articles) {
				wordsProcessor.parseArticlePlainTextAndBuildMapOfWords(article);
				articleRepository.save(article);
			}
		}
		
		LOGGER.info("number of processed articles: " + numberOfArticles);
		
		return numberOfArticles;
	}

	@RequestMapping(value = "/get-article", method = RequestMethod.GET)
	public ArticleDTO getArticle(@RequestParam(value = "id") Long id) {
		LOGGER.info(">>> get article by id: " + id);
		return convertTo(articleRepository.findOne(id));
	}

	@Override
	public Article convertFrom(ArticleDTO obj) {
		return new Article(obj.getRawText(), obj.getTitle(), obj.getDateTime(),
				obj.getUrl());
	}

	@Override
	public ArticleDTO convertTo(Article obj) {
		return new ArticleDTO(obj.getRawText(), obj.getTitle(),
				obj.getDateTime(), obj.getUrl());
	}
}
