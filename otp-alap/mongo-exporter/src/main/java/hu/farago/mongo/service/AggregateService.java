package hu.farago.mongo.service;

import hu.farago.mongo.flatten.AggregateRunner;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AggregateService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AggregateService.class);
	
	@Autowired
	private AggregateRunner runner;
	
	@RequestMapping(value = "/runAllScriptsInDirectory", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public void runAllScriptsInDirectory() {
		LOGGER.info("runAllScriptsInDirectory");
		
		try {
			runner.runAllScriptsInDirectory();
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	@Scheduled(cron = "0 0 8 * * ?")
	public void aggregateAllScheduled() {
		runAllScriptsInDirectory();
	}
	
}
