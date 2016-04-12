package hu.farago.mongo.flatten;

import hu.farago.mongo.AbstractRootTest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.MongoDbFactory;

import com.mongodb.DBCollection;

public class AggregateRunnerTest extends AbstractRootTest {

	private static final String EDGAR_DATA_FLAT = "edgar_data_flat";

	@Autowired
	private AggregateRunner aggregateRunner;
	
	@Autowired
	private MongoDbFactory mongo;
	
	@Value("${mongo.db}")
	private String database;
	
	@Before
	public void before() {
		DBCollection coll = mongo.getDb().getCollection(EDGAR_DATA_FLAT);
		
		if (coll != null) {
			coll.drop();
		}
	}
	
	@Test
	public void executeScriptTest() throws IOException, InterruptedException {
		
		aggregateRunner.executeScript("c:/DEV/export_all/mongo_scripts/edgarDataFlat.js");
		DBCollection coll = mongo.getDb().getCollection(EDGAR_DATA_FLAT);
		
		Assert.assertNotNull(coll);
		Assert.assertNotEquals(0, coll.count());
	}
	
}
