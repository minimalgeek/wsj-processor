package hu.farago.data.nasdaq;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.ShortInterestRepository;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HistoricalShortInterestLoaderTest extends AbstractRootTest {

	@Autowired
	private HistoricalShortInterestLoader loader;
	
	@Autowired
	private ShortInterestRepository repository;
	
	@Before
	public void before() {
		repository.deleteAll();
	}
	
	@Test
	public void importAllHistoricalDataFromDirectoryTest() throws IOException {
		loader.importAllHistoricalDataFromDirectory();
		Assert.assertTrue(repository.count() > 0);
	}
	
}
