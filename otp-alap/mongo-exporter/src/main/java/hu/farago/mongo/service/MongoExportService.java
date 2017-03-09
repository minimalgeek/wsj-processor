package hu.farago.mongo.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import hu.farago.mongo.exporter.MongoExporter;

@Controller
public class MongoExportService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MongoExportService.class);
	
	@Autowired
	private MongoExporter mongoExporter;

	public void exportEarningsCall() {
		LOGGER.info("exportEarningsCall");
		
		try {
			mongoExporter.exportEarningsCall();
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
		}
	}

}
