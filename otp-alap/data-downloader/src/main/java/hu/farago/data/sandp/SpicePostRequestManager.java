package hu.farago.data.sandp;

import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;
import hu.farago.data.sandp.dto.ResponseJSON;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Maps;

@Component
public class SpicePostRequestManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SpicePostRequestManager.class);

	@Autowired
	private SpiceHttpMethodBuilder methodBuilder;

	private HttpClient httpClient;

	@PostConstruct
	private void postConstruct() {
		httpClient = new HttpClient();
	}

	public Map<SAndPGroup, ResponseJSON> downloadAllIndices()
			throws HttpException, IOException {
		Map<SAndPGroup, ResponseJSON> responses = Maps.newHashMap();

		responses.put(SAndPGroup.SP100, getData(SAndPGroup.SP100));
		responses.put(SAndPGroup.SP400, getData(SAndPGroup.SP400));
		responses.put(SAndPGroup.SP500, getData(SAndPGroup.SP500));
		responses.put(SAndPGroup.SP600, getData(SAndPGroup.SP600));

		return responses;
	}

	private ResponseJSON getData(SAndPGroup group) throws HttpException,
			IOException {
		HttpMethod method = methodBuilder.buildMethod(group);

		httpClient.executeMethod(method);
		String response = method.getResponseBodyAsString();
		LOGGER.info(response);
		
		ResponseJSON json = createResponseJSONFromContentString(response);
		return json;
	}

	private ResponseJSON createResponseJSONFromContentString(String content)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JodaModule());
		return objectMapper.readValue(content, ResponseJSON.class);
	}

}
