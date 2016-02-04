package hu.farago.mongo.service;

import static org.junit.Assert.assertTrue;
import hu.farago.mongo.AbstractRootTest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MongoExportServiceTest extends AbstractRootTest {

	@Autowired
	private MongoExportService mongoExportService;
	
	@Value("${exporter.path}")
	private String exportPath;
	
	@Value("${exporter.earningsCallPath}")
	private String earningsCallPath;

	@Test
	public void testExportEarningsCall() throws IOException {
		File dir = new File(exportPath + earningsCallPath);
		mongoExportService.exportEarningsCall();
		
		assertTrue(directoryIsEmpty(dir));
		FileUtils.cleanDirectory(dir);
	}
	
	private boolean directoryIsEmpty(File directory) {
		if(directory.isDirectory()){
			return (directory.list().length > 0);
		} else {
			return false;	
		}
	}

}
