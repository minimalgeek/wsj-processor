package hu.farago.data.service;

import hu.farago.data.model.dao.mongo.SAndPIndexRepository;
import hu.farago.data.model.entity.mongo.SAndPIndex;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;
import hu.farago.data.sandp.SpicePostRequestManager;
import hu.farago.data.sandp.SpiceToSAndPMapper;
import hu.farago.data.sandp.dto.CompanyJSON;
import hu.farago.data.sandp.dto.ResponseJSON;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SAndPIndicesRefreshService {

	@Autowired
	private SpicePostRequestManager requestBuilder;

	@Autowired
	private SpiceToSAndPMapper mapper;

	@Autowired
	private SAndPIndexRepository repository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SAndPIndicesRefreshService.class);

	@RequestMapping(value = "/refreshSAndPIndices", method = RequestMethod.GET)
	public void refreshSAndPIndices() {
		LOGGER.info("refreshSAndPIndices");

		try {
			Map<SAndPGroup, ResponseJSON> mapOfResponses = requestBuilder
					.downloadAllIndices();
			for (Map.Entry<SAndPGroup, ResponseJSON> entry : mapOfResponses
					.entrySet()) {
				LOGGER.info("Saving " + entry.getKey().getName());

				for (CompanyJSON company : entry.getValue().companies) {
					if (StringUtils.isNotEmpty(company.eventName) && 
						StringUtils.isNotEmpty(company.currentTicker)) {
						SAndPIndex index = mapper.map(company);
						if (index != null) {
							processIndex(index);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void processIndex(SAndPIndex index) {
		SAndPIndex foundIndex = repository
				.findByTradingSymbol(index.tradingSymbol);

		if (foundIndex != null) {
			foundIndex.merge(index);
			repository.save(foundIndex);
		} else {
			repository.save(index);
		}
	}

	// every day at midnight
	@Scheduled(cron = "0 0 12 * * ?")
	public void lookForTranscriptsScheduled() {
		refreshSAndPIndices();
	}

}
