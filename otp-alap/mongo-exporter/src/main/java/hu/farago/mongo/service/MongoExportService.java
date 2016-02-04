package hu.farago.mongo.service;

import hu.farago.mongo.exporter.MongoExporter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MongoExportService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MongoExportService.class);
	
	@Autowired
	private MongoExporter mongoExporter;

	@RequestMapping(value = "/exportEarningsCall", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public void exportEarningsCall() {
		LOGGER.info("exportEarningsCall");
		
		try {
			mongoExporter.exportEarningsCall();
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
		}
	}

}
