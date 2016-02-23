package hu.farago.data.seekingalpha;

import static org.junit.Assert.assertThat;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.model.entity.mongo.InsiderData;
import hu.farago.data.model.entity.mongo.InsiderData.BuySell;
import hu.farago.data.model.entity.mongo.InsiderData.OwnerRelationShip;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public class EarningsCallAndInsiderDataAggregatorTest extends AbstractRootTest {

	@Autowired
	private EarningsCallAndInsiderDataAggregator aggregator;
	
	private EarningsCall sample, samplePrevious;
	private List<InsiderData> datas;
	
	@Before
	public void before() {
		sample = new EarningsCall();
		sample.publishDate = new DateTime(2015, 12, 30, 12, 00);
		
		samplePrevious = new EarningsCall();
		samplePrevious.publishDate = new DateTime(2015, 07, 30, 12, 00);
		
		datas = Lists.newArrayList();
		
		datas.add(createInsiderData("Peti", 20.0, 40.0, new DateTime(2015, 10, 10, 12, 00), BuySell.BUY));
		datas.add(createInsiderData("Peti", 30.0, 20.0, new DateTime(2015, 10, 15, 12, 00), BuySell.BUY));
		datas.add(createInsiderData("Peti", 10.0, 30.0, new DateTime(2015, 9, 5, 12, 00), BuySell.SELL));
		datas.add(createInsiderData("Peti", 15.0, 40.0, new DateTime(2015, 9, 10, 12, 00), BuySell.SELL));
		datas.add(createInsiderData("Peti", 20.0, 50.0, new DateTime(2015, 9, 15, 12, 00), BuySell.SELL));
		
		datas.add(createInsiderData("Zoli", 20.0, 40.0, new DateTime(2016, 1, 1, 12, 00), BuySell.BUY));
		datas.add(createInsiderData("Zoli", 50.0, 10.0, new DateTime(2015, 10, 16, 12, 00), BuySell.BUY));
		datas.add(createInsiderData("Zoli", 60.0, 50.0, new DateTime(2015, 9, 6, 12, 00), BuySell.BUY));
		datas.add(createInsiderData("Zoli", 70.0, 80.0, new DateTime(2015, 9, 11, 12, 00), BuySell.SELL));
		datas.add(createInsiderData("Zoli", 20.0, 50.0, new DateTime(2014, 1, 1, 12, 00), BuySell.SELL));
		
		datas.add(createInsiderData("Márk", 20.0, 40.0, new DateTime(2013, 5, 8, 12, 00), BuySell.BUY));
		datas.add(createInsiderData("Márk", 30.0, 50.0, new DateTime(2013, 5, 10, 12, 00), BuySell.SELL));
	}
	
	@Test
	public void processCallTest() {
		aggregator.processCall(sample, samplePrevious, datas);
		
		assertThat(sample.sumOfSharesOwnedBeforePublishDate, new IsEqual<Double>(30.0 + 50.0));
		assertThat(sample.sumOfBuyTransactionShares, new IsEqual<Double>(20.0 + 40.0 + 10.0 + 50.0));
		assertThat(sample.sumOfSellTransactionShares, new IsEqual<Double>(30.0 + 40.0 + 50.0 + 80.0));
	}
	
	private InsiderData createInsiderData(String ownerName, double sharesOwned, double transactionShares, DateTime acceptanceDate, BuySell type) {
		InsiderData ret = new InsiderData();
		
		ret.reportingOwnerName = ownerName;
		ret.sharesOwned = sharesOwned;
		ret.transactionShares = transactionShares;
		ret.acceptanceDate = acceptanceDate;
		ret.type = type;
		ret.ownerRelationShip = OwnerRelationShip.OFFICER;
		
		return ret;
	}
	
}
