package hu.farago.data.seekingalpha;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.EarningsCallRepository;
import hu.farago.data.model.dao.mongo.HarvardWordsRepository;
import hu.farago.data.model.dao.mongo.HenryWordsRepository;
import hu.farago.data.model.entity.mongo.EarningsCall;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EarningsCallWordCounterTest extends AbstractRootTest {
	
	private static EarningsCall sampleCall;

	@Autowired
	private HarvardWordsRepository harvardWR;
	
	@Autowired
	private HenryWordsRepository henryWR;
	
	@Autowired
	private EarningsCallRepository eCR;
	
	@BeforeClass
	public static void beforeClass() {
		sampleCall = eCR.
	}
	
	@Test
	public void collectToneOneForSampleArticle() {
		
	}
	
}
