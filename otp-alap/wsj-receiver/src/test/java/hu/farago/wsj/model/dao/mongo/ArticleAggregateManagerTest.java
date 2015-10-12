package hu.farago.wsj.model.dao.mongo;

import hu.farago.wsj.config.AbstractRootTest;
import hu.farago.wsj.controller.dto.CompanyInfoDTO;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ArticleAggregateManagerTest extends AbstractRootTest {

	@Autowired
	private ArticleAggregateManager aggregateManager;
	
	private CompanyInfoDTO aapl;
	
	@Before
	public void before() {
		aapl = new CompanyInfoDTO("AAPL", "APPLE INC");
	}
	
	@Test
	public void findAllByFullTextSearchTest() {
		List<ArticleCollection> articles = aggregateManager.findAllByFullTextSearch(aapl);
		assertThat(articles, notNullValue());
	}

}
