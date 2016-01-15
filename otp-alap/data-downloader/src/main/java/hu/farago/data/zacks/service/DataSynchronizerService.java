package hu.farago.data.zacks.service;

import hu.farago.data.utils.URLUtils;
import hu.farago.data.zacks.file.ZacksFileUtils;
import hu.farago.data.zacks.service.dto.ZacksData;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RestController
public class DataSynchronizerService {
	
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
				String content = URLUtils.getContentForURL(url);
				ZacksData zacksData = createZacksDataFromContent(content);
				zacksFileUtils.writeZacksDataToCSVFiles(zacksData);
				
				refreshedURLs.add(url);
			} catch (Exception ex) {
				LOGGER.error("Exception happened during URL content open or processing", ex);
			}
		}
		
		return refreshedURLs;
	}
	

	public ZacksData createZacksDataFromContent(String massContent) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(massContent, ZacksData.class);
	}
	
	

	
}
