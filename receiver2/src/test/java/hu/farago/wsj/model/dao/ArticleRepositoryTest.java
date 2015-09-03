package hu.farago.wsj.model.dao;

import hu.farago.wsj.config.AbstractRootTest;
import hu.farago.wsj.model.dao.sql.ArticleRepository;
import hu.farago.wsj.model.entity.sql.Article;
import hu.farago.wsj.model.entity.sql.ArticleWord;

import java.util.Date;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ArticleRepositoryTest extends AbstractRootTest {
	
	@Autowired
	private ArticleRepository articleRepository;

	@Test
	public void testSave() {
		Article article = new Article();
		article.setRawText("raw text test");
		article.setDateTime(new Date());
		article.setTitle("title test");
		article.setUrl("http://localhost:8080/hello.jsp");
		articleRepository.save(article);
		
		MatcherAssert.assertThat(articleRepository.findAll(), IsIterableWithSize.<Article>iterableWithSize(1));
	}
	
	@Test
	public void testSaveWithWords() {
		Article article = new Article();
		article.setRawText("<p>raw text test</p>");
		article.setDateTime(new Date());
		article.setTitle("title test");
		article.setUrl("http://localhost:8080/hello.jsp");
		article.setPlainText("raw text test");
		
		article.addArticleWord(new ArticleWord("world"));
		article.addArticleWord(new ArticleWord("is"));
		article.addArticleWord(new ArticleWord("mine"));
		
		articleRepository.save(article);
		
		MatcherAssert.assertThat(articleRepository.findAll(), IsIterableWithSize.<Article>iterableWithSize(1));
		MatcherAssert.assertThat(articleRepository.findByUrlAndFetchWords("http://localhost:8080/hello.jsp").getArticleWords(), 
				IsIterableWithSize.<ArticleWord>iterableWithSize(3));
	}
	
	@After
	public void after() {
		articleRepository.deleteAll();
	}

}
