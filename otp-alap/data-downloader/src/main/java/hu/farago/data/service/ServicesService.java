package hu.farago.data.service;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.farago.data.model.dao.mongo.AutomaticServiceErrorRepository;
import hu.farago.data.model.entity.mongo.AutomaticServiceError;

@RestController
public class ServicesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesService.class);
	
	@Autowired
	private AutomaticServiceErrorRepository repo;
	
	@RequestMapping(value = "/getErrors", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<AutomaticServiceError> getErrors() {
		LOGGER.info("getErrors");
		return repo.findAll();
	}
	
}
