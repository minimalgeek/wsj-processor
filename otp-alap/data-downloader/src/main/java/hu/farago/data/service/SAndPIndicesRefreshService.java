package hu.farago.data.service;

import hu.farago.data.model.dao.mongo.SAndPIndexRepository;
import hu.farago.data.model.entity.mongo.SAndPIndex;
import hu.farago.data.model.entity.mongo.AutomaticServiceError.AutomaticService;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;
import hu.farago.data.sandp.SAndPFileWriter;
import hu.farago.data.sandp.SpicePostRequestManager;
import hu.farago.data.sandp.SpiceToSAndPMapper;
import hu.farago.data.sandp.dto.CompanyJSON;
import hu.farago.data.sandp.dto.ResponseJSON;
import hu.farago.data.utils.AutomaticServiceErrorUtils;

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

	@Autowired
	private SAndPFileWriter fileWriter;

	@Autowired
	private AutomaticServiceErrorUtils aseu;

	private static final Logger LOGGER = LoggerFactory.getLogger(SAndPIndicesRefreshService.class);

	@RequestMapping(value = "/refreshSAndPIndices", method = RequestMethod.GET)
	public void refreshSAndPIndices() {
		LOGGER.info("refreshSAndPIndices");

		try {
			fileWriter.reloadFileDatas();

			Map<SAndPGroup, ResponseJSON> mapOfResponses = requestBuilder.downloadAllIndices();
			for (Map.Entry<SAndPGroup, ResponseJSON> entry : mapOfResponses.entrySet()) {
				String indexGroupName = entry.getKey().getName();
				LOGGER.info("Saving " + indexGroupName);

				for (CompanyJSON company : entry.getValue().companies) {
					if (StringUtils.isNotEmpty(company.eventName) && StringUtils.isNotEmpty(company.currentTicker)) {
						SAndPIndex index = mapper.map(company);
						if (indexIsValid(index)) {
							processIndex(index);
						}
					}
				}
			}

			fileWriter.writeFileDatas();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			aseu.saveError(AutomaticService.SPICE_INDICES, e.getMessage());
		}
	}

	private boolean indexIsValid(SAndPIndex index) {
		return index != null && !index.tradingSymbol.equals("--");
	}

	private void processIndex(SAndPIndex index) throws Exception {
		SAndPIndex foundIndex = repository.findByTradingSymbol(index.tradingSymbol);

		if (foundIndex != null) {
			foundIndex.merge(index);
			repository.save(foundIndex);
			fileWriter.addSAndPIndex(foundIndex);
		} else {
			repository.save(index);
			fileWriter.addSAndPIndex(index);
		}
	}

	// every day at midnight
	@Scheduled(cron = "0 0 12 * * ?")
	public void refreshSAndPIndicesScheduled() {
		try {
			refreshSAndPIndices();
		} catch (Exception e) {
			aseu.saveError(AutomaticService.SPICE_INDICES, e.getMessage());
		}
	}

}
