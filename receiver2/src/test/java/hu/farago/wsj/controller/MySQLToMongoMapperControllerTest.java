package hu.farago.wsj.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import hu.farago.wsj.config.AbstractRootTest;

public class MySQLToMongoMapperControllerTest extends AbstractRootTest {
	
	@Autowired
	private MySQLToMongoMapperController myController;
	
	@Test
	public void testProcessAll() {
		myController.moveArticles();
	}

}
