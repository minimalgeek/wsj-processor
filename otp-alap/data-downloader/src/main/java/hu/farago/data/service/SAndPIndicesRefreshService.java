package hu.farago.data.service;

import hu.farago.data.model.dao.mongo.SAndPIndexRepository;
import hu.farago.data.sandp.SAndPDownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SAndPIndicesRefreshService {

	@Autowired
	private SAndPDownloader downloader;
	
	@Autowired
	private SAndPIndexRepository repository;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SAndPIndicesRefreshService.class);

	@RequestMapping(value = "/refreshSAndPIndices", method = RequestMethod.GET)
	public void refreshSAndPIndices() {
		LOGGER.info("refreshSAndPIndices");
		
		try {
			repository.save(downloader.processFirstPage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
