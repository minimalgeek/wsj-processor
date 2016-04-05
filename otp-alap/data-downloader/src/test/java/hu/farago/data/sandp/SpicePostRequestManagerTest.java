package hu.farago.data.sandp;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.SAndPIndex;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;
import hu.farago.data.sandp.dto.CompanyJSON;
import hu.farago.data.sandp.dto.ResponseJSON;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SpicePostRequestManagerTest extends AbstractRootTest {

	@Autowired
	private SpicePostRequestManager manager;
	
	@Autowired
	private SpiceToSAndPMapper mapper;
	
	@Test
	public void downloadAllIndicesTest() throws HttpException, IOException {
		Map<SAndPGroup, ResponseJSON> ret =  manager.downloadAllIndices();
		Assert.assertNotNull(ret);
		Assert.assertEquals(4, ret.size());
	}
	
	@Test
	public void mapTest() {
		CompanyJSON company = new CompanyJSON();
		company.closeAsOfDate = new Date();
		company.currentCompanyName = "XYZ Kft";
		company.currentTicker = "XYZ";
		company.eventName = "Add";
		company.indexId = 340;
		company.indexKey = "SPUSA-500-USDUF--P-US-L--";
		company.indexName = "S&P 500";
		company.lastUpdated = "Mar 29, 2016 10:00 AM";
		
		SAndPIndex index = mapper.map(company);
		
		Assert.assertEquals(1, index.operations.size());
		Assert.assertEquals("XYZ", index.tradingSymbol);
		Assert.assertEquals(SAndPGroup.SP500, index.operations.get(0).indexGroup);
	}
	
}
