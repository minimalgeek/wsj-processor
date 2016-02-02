package hu.farago.data.seekingalpha;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.dao.mongo.EarningsCallRepository;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.seekingalpha.dto.HTone;
import hu.farago.data.seekingalpha.dto.Tone;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EarningsCallWordCounterTest extends AbstractRootTest {
	
	private EarningsCall sampleCall;
	
	@Autowired
	private EarningsCallRepository eCR;
	
	@Autowired
	private ToneCalculator toneCalculator;
	
	@Before
	public void beforeClass() {
		if (sampleCall == null) {
			sampleCall = eCR.findOne(new BigInteger("56aa239435f7401f44956ada", 16));
		}
	}
	
	@Test
	public void collectToneForSampleArticle() {
		Tone tone = toneCalculator.getToneOf(sampleCall);
		Assert.assertNotNull(tone);
		
		Assert.assertNotEquals(tone.getToneOneAP(), 0);
		Assert.assertNotEquals(tone.getToneOneOU(), 0);
		Assert.assertNotEquals(tone.getToneOnePN(), 0);
		Assert.assertNotEquals(tone.getToneOneSW(), 0);
		
		Assert.assertNotEquals(tone.getToneTwoAP(), 0);
		Assert.assertNotEquals(tone.getToneTwoOU(), 0);
		Assert.assertNotEquals(tone.getToneTwoPN(), 0);
		Assert.assertNotEquals(tone.getToneTwoSW(), 0);
	}
	
	@Test
	public void collectHToneForSampleArticle() {
		HTone tone = toneCalculator.getHToneOf(sampleCall);
		Assert.assertNotNull(tone);
		
		Assert.assertNotEquals(tone.getHToneOnePN(), 0);
		Assert.assertNotEquals(tone.getHToneTwoPN(), 0);
	}
	
}
