package hu.farago.data.service;

import hu.farago.data.model.dao.mongo.OilReportRepository;
import hu.farago.data.oilreport.OilReportDownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OilReportService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OilReportService.class);
	
	@Autowired
	private OilReportDownloader downloader;
	@Autowired
	private OilReportRepository repositroy;
	
	@RequestMapping(value = "/downloadOilReports", method = RequestMethod.GET)
	public void downloadOilReports() {
		try {
			downloader.downloadAndSaveAll();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
