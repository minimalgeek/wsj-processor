package hu.farago.wsj.parse.wordprocess;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import hu.farago.wsj.model.entity.Article;
import hu.farago.wsj.model.entity.ArticleWord;

public abstract class WordsProcessor {

	public void parseArticlePlainTextAndBuildMapOfWords(Article existingArticle) {
		preParseCheck(existingArticle);
		try {
			doBuild(existingArticle);
			existingArticle.setProcessed(true);
		} catch (Exception e) {
			getLogger().error(e.getMessage());
			existingArticle.setProcessed(false);
		}
	}

	protected abstract void doBuild(Article existingArticle) throws Exception;

	protected void addWordToArticle(Article existingArticle, String word) {
		getLogger().debug(word);
		ArticleWord articleWord = new ArticleWord();
		articleWord.setWord(StringUtils.lowerCase(word));
		existingArticle.addArticleWord(articleWord);
	}

	protected void preParseCheck(Article existingArticle) {
		Assert.notNull(existingArticle);
		Assert.notNull(existingArticle.getPlainText());

		existingArticle.setArticleWords(new HashSet<ArticleWord>());
	}

	protected abstract Logger getLogger();

}
