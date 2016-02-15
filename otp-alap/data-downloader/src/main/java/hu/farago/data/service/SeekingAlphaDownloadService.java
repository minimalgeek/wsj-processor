package hu.farago.data.service;

import hu.farago.data.model.dao.mongo.EarningsCallRepository;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.seekingalpha.SeekingAlphaDownloader;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class SeekingAlphaDownloadService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SeekingAlphaDownloadService.class);

	@Autowired
	private SeekingAlphaDownloader seekingAlphaDownloader;
	@Autowired
	private EarningsCallRepository earningsCallRepository;

	@RequestMapping(value = "/collectEarningsCalls", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> collectEarningsCalls() {
		LOGGER.info("collectEarningsCalls");
		
		List<String> ret = Lists.newArrayList();
		try {
			earningsCallRepository.deleteAll();
			for (int i = 0; i < seekingAlphaDownloader.pages(); i++) {
				Map<String, List<EarningsCall>> map = seekingAlphaDownloader.parseAll(i);
				for (Map.Entry<String, List<EarningsCall>> entry : map.entrySet()) {
					earningsCallRepository.save(entry.getValue());
				}
				
				ret.addAll(map.keySet());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return ret;
	}

	@RequestMapping(value = "/processOnlyQAndA", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> processOnlyQAndA() {
		LOGGER.info("processOnlyQAndA");
		
		List<String> ret = Lists.newArrayList();
		try {
			for (String index : seekingAlphaDownloader.getIndexes()) {
				List<EarningsCall> calls = earningsCallRepository.findByTradingSymbol(index);
				
				for (EarningsCall call : calls) {
					seekingAlphaDownloader.retrieveRelevantQAndAPartAndProcessTone(call);
				}
				
				earningsCallRepository.save(calls);
				LOGGER.info(index + " processed");
				ret.add(index);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return ret;
	}
	
}
