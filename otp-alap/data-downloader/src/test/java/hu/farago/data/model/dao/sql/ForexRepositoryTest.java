package hu.farago.data.model.dao.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.ForexRepository;
import hu.farago.data.model.entity.mongo.Forex;
import hu.farago.data.model.entity.mongo.QForex;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Iterables;

public class ForexRepositoryTest extends AbstractRootTest {

	private static final String EURHUF = "EURHUF";
	private static final String EURUSD = "EURUSD";

	@Autowired
	private ForexRepository forexRepo;

	private Forex forexTestData;
	private Forex forexTestData2;

	@Before
	public void setUp() throws Exception {
		forexTestData = new Forex(EURHUF, getNewDate(2015, 1, 2), 270.45, 273.77, 269.54,
				270.69, 13.23);
		
		forexTestData2 = new Forex(EURHUF, getNewDate(2015, 1, 3), 273.45, 276.77, 270.33,
				271.80, 12.0);
		
		forexRepo.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
		forexRepo.deleteAll();
	}

	@Test
	public void saveTest() {
		Forex returnData = forexRepo.save(forexTestData);

		assertNotNull(returnData.id);
	}

	@Test
	public void findOneTest() {
		Forex returnData = forexRepo.save(forexTestData);

		assertNotNull(forexRepo.findOne(returnData.id));
	}
	
	@Test
	public void deleteByDateAndSymbolTest() {
		forexRepo.save(forexTestData);
		forexRepo.save(forexTestData2);
		assertEquals(2, Iterables.size(forexRepo.findAll()));

		deleteByDateAndSymbol(getNewDate(2015, 1, 2), EURUSD);
		assertEquals(2, Iterables.size(forexRepo.findAll()));
		
		deleteByDateAndSymbol(getNewDate(2015, 1, 2), EURHUF);
		assertEquals(1, Iterables.size(forexRepo.findAll()));
		
		deleteByDateAndSymbol(getNewDate(2015, 1, 3), EURHUF);
		assertEquals(0, Iterables.size(forexRepo.findAll()));
	}
	
	private void deleteByDateAndSymbol(DateTime date, String symbol) {
		QForex forexPredicate = QForex.forex;
		Iterable<Forex> foundForexes = forexRepo
				.findAll(forexPredicate.symbol.eq(symbol).and(
						forexPredicate.tickDate.eq(date)));
		forexRepo.delete(foundForexes);
	}

}
