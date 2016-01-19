package hu.farago.data.insider.service;

import hu.farago.data.insider.dao.mongo.InsiderDataDAO;
import hu.farago.data.insider.dto.InsiderData;
import hu.farago.data.insider.file.InsiderFileUtils;
import hu.farago.data.insider.parser.InsiderTradingDownloader;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsiderTradingDownloadService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InsiderTradingDownloadService.class);

	@Autowired
	private InsiderTradingDownloader insiderTradingParser;
	@Autowired
	private InsiderFileUtils insiderFileUtils;
	@Autowired
	private InsiderDataDAO insiderDataDao;

	@Scheduled(cron = "0 0 12 * * ?") // every day at 12:00
	public void scheduledCollectContent() {
		collectContent();
	}

	@RequestMapping(value = "/collectContent", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void collectContent() {
		LOGGER.info("collectContent");
		try {
			insiderDataDao.deleteAll();
			for (int i = 0; i < InsiderTradingDownloader.pages(); i++) {
				Map<String, List<InsiderData>> map = insiderTradingParser.parseAll(i);
				insiderFileUtils.writeInsiderDataToCSVFiles(map);
				for (Map.Entry<String, List<InsiderData>> entry : map.entrySet()) {
					insiderDataDao.save(entry.getValue());
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
