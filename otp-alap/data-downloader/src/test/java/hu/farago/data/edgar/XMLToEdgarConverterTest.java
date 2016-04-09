package hu.farago.data.edgar;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.edgar.dto.EdgarXML;
import hu.farago.data.model.dao.mongo.EdgarDataRepository;
import hu.farago.data.model.entity.mongo.EdgarData;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class XMLToEdgarConverterTest extends AbstractRootTest {

	private static final String SAMPLE_XML = "xml_samples/apple_2016_04_01.xml";
	private File sampleXmlFile;

	
	@Autowired
	private XMLToEdgarConverter converter;
	
	@Autowired
	private EdgarDataRepository repo;
	
	@Before
	public void before() throws Exception {
		URL url = Thread.currentThread().getContextClassLoader()
			    .getResource(SAMPLE_XML);
		
		sampleXmlFile = FileUtils.toFile(url);
	}
	
	@Test
	public void convertXMLToEdgarTest() throws JAXBException {
		EdgarXML edgar = converter.convertXMLToEdgar(sampleXmlFile);
		
		Assert.assertNotNull(edgar);
		Assert.assertEquals("X0306", edgar.schemaVersion);
		Assert.assertEquals("AAPL", edgar.issuer.issuerTradingSymbol);
		Assert.assertTrue(edgar.reportingOwner.reportingOwnerRelationship.isOfficer);
		Assert.assertFalse(edgar.reportingOwner.reportingOwnerRelationship.isDirector);
		Assert.assertEquals(3, edgar.nonDerivativeTable.size());
		Assert.assertEquals(1, edgar.derivativeTable.size());
		Assert.assertEquals("2016-04-05", edgar.ownerSignature.signatureDate.toString("yyyy-MM-dd"));
	}
	
	@Test
	public void saveTest() throws JAXBException {
		EdgarXML edgarXML = converter.convertXMLToEdgar(sampleXmlFile);
		EdgarData data = new EdgarData();
		data.edgarXML = edgarXML;
		data.tradingSymbol = "AAPL";
		data.formURL = "http://whatever";
		
		repo.save(data);
	}
	
}
