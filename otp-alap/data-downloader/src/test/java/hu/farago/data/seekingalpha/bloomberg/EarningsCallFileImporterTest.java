package hu.farago.data.seekingalpha.bloomberg;

import static org.junit.Assert.*;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.EarningsCallRepository;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EarningsCallFileImporterTest extends AbstractRootTest {

	@Autowired
	private EarningsCallFileImporter importer;
	
	@Autowired
	private EarningsCallRepository repo;
	
	@Before
	public void before() {
		repo.deleteAll();
	}
	
	@Test
	public void importAllTest() {
		importer.importAll();
		assertEquals(repo.count(), 37);
	}
}
