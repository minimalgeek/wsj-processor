package hu.farago.data.service;

import hu.farago.data.macroman.MacroManDownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MacroManService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MacroManService.class);
	
	@Autowired
	private MacroManDownloader downloader;
	
	@RequestMapping(value = "/downloadMacroMans", method = RequestMethod.GET)
	public void downloadMacroMans() {
		try {
			downloader.downloadAndSaveAll();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
