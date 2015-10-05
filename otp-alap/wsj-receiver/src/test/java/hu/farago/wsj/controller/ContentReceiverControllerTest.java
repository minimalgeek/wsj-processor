package hu.farago.wsj.controller;

import static org.junit.Assert.*;
import hu.farago.wsj.config.AbstractRootTest;
import hu.farago.wsj.controller.dto.ArticleDTO;
import hu.farago.wsj.model.dao.mongo.ArticleCollectionManager;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentReceiverControllerTest extends AbstractRootTest {
	
	private static final String HELLO_RAW_TEXT = "hello raw text";
	private static final String BRAND_NEW_RAW_TEXT = "brand new raw text";
	@Autowired
	private ContentReceiverController contentRC;
	@Autowired
	private ArticleCollectionManager collectionManager;

	private ArticleDTO dto1;
	
	@Before
	public void before() {
		dto1 = new ArticleDTO();
		
		dto1.setDateTime(getNewDate(2015, 2, 12));
		dto1.setRawText(HELLO_RAW_TEXT);
		dto1.setTitle("hello title");
		dto1.setUrl("http://whatever.com/something");
	}
	
	@After
	public void after() {
		collectionManager.deleteAll();
	}
	
	@Test
	public void saveArticleTest() {
		contentRC.saveArticle(dto1);
		assertEquals(1, collectionManager.count());
	}
	
	@Test
	public void modifyArticleTest() {
		contentRC.saveArticle(dto1);
		assertEquals(1, collectionManager.count());
		ArticleCollection tempDTO = collectionManager.findAll().get(0);
		assertEquals(HELLO_RAW_TEXT, tempDTO.getRawText());
		
		dto1.setRawText(BRAND_NEW_RAW_TEXT);
		contentRC.saveArticle(dto1);
		assertEquals(1, collectionManager.count());
		tempDTO = collectionManager.findAll().get(0);
		assertEquals(BRAND_NEW_RAW_TEXT, tempDTO.getRawText());
	}
	
}
