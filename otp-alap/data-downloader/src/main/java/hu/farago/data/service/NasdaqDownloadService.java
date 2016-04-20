package hu.farago.data.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NasdaqDownloadService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NasdaqDownloadService.class);
	
	@RequestMapping(value = "/downloadShortInterestData", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<String> downloadShortInterestData() {
		LOGGER.info("downloadShortInterestData");
		return null;
	}
}
