package hu.farago.data.sandp;

import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.model.entity.mongo.SAndPIndex;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.Event;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SAndPFileWriterTest extends AbstractRootTest {

	@Autowired
	private SAndPFileWriter writer;
	
	private static DateTimeZone defaultZone;
	
	@BeforeClass
	public static void beforeClass() {
		defaultZone = DateTimeZone.getDefault();
		DateTimeZone.setDefault(DateTimeZone.UTC);
	}
	
	@AfterClass
	public static void afterClass() {
		DateTimeZone.setDefault(defaultZone);
	}
	
	@Test
	public void wholePipelineTest() throws Exception {
		writer.reloadFileDatas();
		
		writer.addSAndPIndex(createSAndPIndex("XYZ", SAndPGroup.SP500, Event.ADD, new DateTime(2016, 1, 2, 0 , 0)));
		writer.addSAndPIndex(createSAndPIndex("XYZ", SAndPGroup.SP500, Event.DROP, new DateTime(2016, 1, 5, 0 , 0)));
		writer.addSAndPIndex(createSAndPIndex("XYZ", SAndPGroup.SP500, Event.ADD, new DateTime(2016, 2, 20, 0 , 0)));
		writer.addSAndPIndex(createSAndPIndex("ABC", SAndPGroup.SP500, Event.DROP, new DateTime(2003, 10, 5, 0 , 0)));
		writer.addSAndPIndex(createSAndPIndex("ADP", SAndPGroup.SP500, Event.DROP, new DateTime(2015, 11, 25, 0 , 0)));
		writer.addSAndPIndex(createSAndPIndex("AFS", SAndPGroup.SP500, Event.ADD, new DateTime(2014, 5, 5, 0 , 0)));
		writer.addSAndPIndex(createSAndPIndex("YOLO", SAndPGroup.SP400, Event.DROP, new DateTime(2014, 5, 5, 0 , 0)));
		
		writer.addSAndPIndex(createSAndPIndex("BELE", SAndPGroup.SP400, Event.ADD, new DateTime(2014, 5, 5, 0 , 0)));
		writer.addSAndPIndex(createSAndPIndex("FELIGBELE", SAndPGroup.SP600, Event.ADD, new DateTime(2014, 5, 5, 0 , 0)));
		
		writer.writeFileDatas();
	}
	
	private SAndPIndex createSAndPIndex(String ticker, SAndPGroup indGroup, Event event, DateTime dt) {
		SAndPIndex ret = new SAndPIndex();
		ret.company = ticker + " Kft";
		ret.tradingSymbol = ticker;
		
		SAndPOperation op = new SAndPOperation();
		op.indexGroup = indGroup;
		op.event = event;
		op.eventDate = dt;
		
		ret.operations.add(op);
		return ret;
	}
	
}
