package hu.farago.zacks.service;

import hu.farago.zacks.service.dto.CompanyData;
import hu.farago.zacks.service.dto.ZacksData;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class DataSynchronizerService {
	
	private static final String UTF_8 = "UTF-8";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataSynchronizerService.class);

	@Value("#{'${zacks.urls}'.split(',')}")
	private List<String> zacksURLList;
	
	@Value("${zacks.path}")
	private String pathToCSVs;
	
	@Scheduled(fixedDelay = 100000)
	public void refreshAllReportDates() {
		LOGGER.info("refreshAllReportDates");

		for (String url : zacksURLList) {
			try {
				String content = getContentForURL(url);
				ZacksData zacksData = createZacksDataFromContent(content);
				LOGGER.info("ZacksData's title: " + zacksData.getTitle());
				
				processNewData(zacksData);
			} catch (Exception ex) {
				LOGGER.error("I/O exception or SAXException happened during URL open", ex);
			}
		}
	}
	
	public String getContentForURL(String url) throws IOException, SAXException, TikaException {
		ContentHandler handler = new BodyContentHandler(-1);
		String rawText = IOUtils.toString(new URL(url), UTF_8);
        new HtmlParser().parse(IOUtils.toInputStream(rawText), handler, new Metadata(), new ParseContext());
        String plainText = StringUtils.normalizeSpace(handler.toString());
		return plainText;
	}

	public ZacksData createZacksDataFromContent(String massContent) throws JsonParseException, JsonMappingException, IOException {
		String jsonContent = StringUtils.substringBetween(massContent, "growth_portfoliosList\" : ", ", \"value_portfoliosList\"");
		
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonContent, ZacksData.class);
	}
	
	private void processNewData(ZacksData zacksData) throws IOException {
		for (CompanyData company : zacksData.getData()) {
			String fileName = StringUtils.join(pathToCSVs, company.getSymbol(), ".csv");
			String nextReportDate = company.getNextReportDate();
			
			File file = createFileIfNotExists(fileName);
			List<String> fileContent = FileUtils.readLines(file, UTF_8);
			
			boolean exists = false;
			for (String line : fileContent) {
				if (line.equals(nextReportDate)) {
					exists = true;
					break;
				}
			}
			
			if (!exists) {
				fileContent.add(nextReportDate);
			}
			
			FileUtils.writeLines(file, UTF_8, fileContent, false);
		}
	}

	private File createFileIfNotExists(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
}
