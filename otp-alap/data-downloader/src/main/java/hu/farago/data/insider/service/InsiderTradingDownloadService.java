package hu.farago.data.insider.service;

import hu.farago.data.insider.parser.InsiderTradingDownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsiderTradingDownloadService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InsiderTradingDownloadService.class);
	
	private InsiderTradingDownloader insiderTradingParser;
	
	@Scheduled(cron = "0 0 12 * * ?") // every day at 12:00
	public void scheduledCollectContent() {
		collectContent();
	}
	
	@RequestMapping(value = "/collectContent", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public void collectContent() {
		LOGGER.info("collectContent");
		insiderTradingParser.parseAll();
	}

}
