package hu.farago.data.seekingalpha;

import static org.junit.Assert.assertThat;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.EarningsCall;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SeekingAlphaQAndATest extends AbstractRootTest {
	
	@Autowired
	private SeekingAlphaDownloader downloader;
	
	@Test
	public void testQAndASplit1() {
		StringBuilder articleSample = new StringBuilder();
		articleSample.append("abc ");
		articleSample.append("Question-and-Answer");
		articleSample.append(" blabla ");
		articleSample.append("Copyright policy");
		articleSample.append(" wololo");
		articleSample.append(" hakuna");
		
		EarningsCall temp = new EarningsCall();
		temp.rawText = articleSample.toString();
		
		downloader.retrieveRelevantQAndAPartAndProcessTone(temp);
		
		assertThat(temp.qAndAText, Matchers.equalTo(" blabla "));
	}
	
	@Test
	public void testQAndASplit2() {
		StringBuilder articleSample = new StringBuilder();
		articleSample.append("Question-and-answer");
		articleSample.append(" blabla ");
		articleSample.append("Copyright policy:");
		
		EarningsCall temp = new EarningsCall();
		temp.rawText = articleSample.toString();
		
		downloader.retrieveRelevantQAndAPartAndProcessTone(temp);
		
		assertThat(temp.qAndAText, Matchers.equalTo(" blabla "));
	}
	
	@Test
	public void testQAndASplit3() {
		StringBuilder articleSample = new StringBuilder();
		articleSample.append("abc ");
		articleSample.append(" blabla ");
		articleSample.append("copyright policy:");
		articleSample.append(" wololo");
		articleSample.append(" hakuna");
		
		EarningsCall temp = new EarningsCall();
		temp.rawText = articleSample.toString();
		
		downloader.retrieveRelevantQAndAPartAndProcessTone(temp);
		
		assertThat(temp.qAndAText, Matchers.equalTo(null));
	}
	
	@Test
	public void testQAndASplit4() {
		StringBuilder articleSample = new StringBuilder();
		articleSample.append("abc ");
		articleSample.append("question-AND-Answer");
		articleSample.append(" blabla ");
		articleSample.append(" wololo");
		articleSample.append(" hakuna");
		
		EarningsCall temp = new EarningsCall();
		temp.rawText = articleSample.toString();
		
		downloader.retrieveRelevantQAndAPartAndProcessTone(temp);
		
		assertThat(temp.qAndAText, Matchers.equalTo(" blabla  wololo hakuna"));
	}
}
