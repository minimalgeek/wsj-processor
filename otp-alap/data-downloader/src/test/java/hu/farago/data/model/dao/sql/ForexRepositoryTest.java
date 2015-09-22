package hu.farago.data.model.dao.sql;

import static org.junit.Assert.*;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.sql.Forex;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ForexRepositoryTest extends AbstractRootTest {

	@Autowired
	private ForexRepository forexRepo;

	private Forex forexTestData;

	@Before
	public void setUp() throws Exception {
		forexTestData = new Forex("EURHUF", new Date(), 270.45, 273.77, 269.54,
				270.69, 13.23);
		
		forexRepo.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
		forexRepo.deleteAll();
	}

	@Test
	public void testSave() {
		Forex returnData = forexRepo.save(forexTestData);

		assertNotNull(returnData.getId());
	}

	@Test
	public void testFindOne() {
		Forex returnData = forexRepo.save(forexTestData);

		assertNotNull(forexRepo.findOne(returnData.getId()));
	}

}
