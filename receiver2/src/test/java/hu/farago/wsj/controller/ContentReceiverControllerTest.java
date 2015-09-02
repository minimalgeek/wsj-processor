package hu.farago.wsj.controller;

import hu.farago.wsj.config.AbstractRootTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentReceiverControllerTest extends AbstractRootTest {
	
	@Autowired
	private ContentReceiverController contentRC;
	
	@Test
	public void testProcessAll() {
		contentRC.processAllArticle();
	}

}
