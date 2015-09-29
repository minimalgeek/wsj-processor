package hu.farago.data.zacks.service;

import hu.farago.data.zacks.file.ZacksFileUtils;
import hu.farago.data.zacks.service.dto.ZacksData;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RestController
public class DataSynchronizerService {
	
	public static final String UTF_8 = "UTF-8";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataSynchronizerService.class);

	@Value("#{'${zacks.urls}'.split(',')}")
	private List<String> zacksURLList;
	
	@Autowired
	private ZacksFileUtils zacksFileUtils;
	
	@RequestMapping(value = "/refreshAllReportDates", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<String> refreshAllReportDates() {
		LOGGER.info("refreshAllReportDates");
		
		List<String> refreshedURLs = Lists.newArrayList();

		for (String url : zacksURLList) {
			try {
				String content = getContentForURL(url);
				ZacksData zacksData = createZacksDataFromContent(content);
				LOGGER.info("ZacksData's title: " + zacksData.getTitle());
				
				zacksFileUtils.writeZacksDataToCSVFiles(zacksData);
				
				refreshedURLs.add(url);
			} catch (Exception ex) {
				LOGGER.error("Exception happened during URL content open or processing", ex);
			}
		}
		
		return refreshedURLs;
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
	
	

	
}
