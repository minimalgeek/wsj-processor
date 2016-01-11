package hu.farago.data.insider.parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InsiderTradingDownloader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InsiderTradingDownloader.class);
	
	public static List<String> INDEXES;
	
	@Value("${insider.filePath}")
	private String filePath;
	@Value("${insider.urlBase}")
	private String urlBase;
	@Value("#{new java.text.SimpleDateFormat('${insider.dateFormat}').parse('${insider.fromDate}')}")
    private Date fromDate;
	@Value("#{new java.text.SimpleDateFormat('${insider.dateFormat}').parse('${insider.toDate}')}")
    private Date toDate;
	
	public void parseAll() {
		for (String index : INDEXES) {
			// HELLO FROM THE OTHER SIDE
		}
	}
	
	@PostConstruct
	private void readFile() {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(filePath);
		File sAndPFile = new File(url.getFile());
		
		try {
			INDEXES = FileUtils.readLines(sAndPFile, Charset.forName("UTF-8"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
