package hu.farago.data.service;

import hu.farago.data.model.dao.mongo.EarningsCallRepository;
import hu.farago.data.model.dao.mongo.InsiderDataRepository;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.model.entity.mongo.InsiderData;
import hu.farago.data.seekingalpha.EarningsCallAndInsiderDataAggregator;
import hu.farago.data.seekingalpha.SeekingAlphaDownloader;
import hu.farago.data.seekingalpha.YahooStockDownloader;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
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
	@Autowired
	private InsiderDataRepository insiderDataRepo;
	@Autowired
	private EarningsCallAndInsiderDataAggregator aggregator;
	@Autowired
	private YahooStockDownloader stockDownloader;

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
	
	@RequestMapping(value = "/collectEarningsCallsFor/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public String collectEarningsCallsFor(@PathVariable("id") String index) {
		LOGGER.info("collectEarningsCallsFor");
		
		StringBuilder ret = new StringBuilder();

		try {
			List<EarningsCall> list = seekingAlphaDownloader.collectAllDataForIndex(index);
			
			for (EarningsCall call : list) {
				if (call.tone != null && call.stockData == null) {
					seekingAlphaDownloader.retrieveRelevantQAndAPartAndProcessTone(call);
					stockDownloader.addStockData(call);
				}
			}
			
			// remove older entries
			earningsCallRepository.delete(earningsCallRepository.findByTradingSymbol(index));
			earningsCallRepository.save(list);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ret.append(e.getMessage());
		}
		
		if (ret.length() == 0) {
			ret.append("success");
		}
		
		return ret.toString();
	}

	@RequestMapping(value = "/processQAndAAndAddStockData", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> processQAndAAndAddStockData() {
		LOGGER.info("processQAndAAndAddStockData");
		
		List<String> ret = Lists.newArrayList();
		try {
			for (String index : seekingAlphaDownloader.getIndexes()) {
				List<EarningsCall> calls = earningsCallRepository.findByTradingSymbol(index);
				
				for (EarningsCall call : calls) {
					if (call.tone != null && call.stockData == null) {
						seekingAlphaDownloader.retrieveRelevantQAndAPartAndProcessTone(call);
						stockDownloader.addStockData(call);
					}
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
	
	@RequestMapping(value = "/appendInsiderDataToEarningsCall", method = RequestMethod.GET)
	public void appendInsiderDataToEarningsCall() {
		LOGGER.info("appendInsiderDataToEarningsCall");
		
		for (String index : seekingAlphaDownloader.getIndexes()) {
			List<EarningsCall> calls = earningsCallRepository.findByTradingSymbol(index, new Sort(Sort.Direction.ASC, "publishDate"));
			List<InsiderData> insiderDataList = insiderDataRepo.findByIssuerTradingSymbol(index);
			
			EarningsCall previousCall = null;
			
			for (EarningsCall call : calls) {
				if (call.tone != null && call.qAndATone != null && previousCall != null) {
					aggregator.processCall(call, previousCall, insiderDataList);
				}
				
				previousCall = call;
			}
			
			earningsCallRepository.save(calls);
			LOGGER.info(index + " processed");
		}
	}
	
	/*
	@RequestMapping(value = "/calculateYield", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> calculateYield() {
		LOGGER.info("calculateYield");
		
		List<String> ret = Lists.newArrayList();
		try {
			for (String index : seekingAlphaDownloader.getIndexes()) {
				List<EarningsCall> calls = earningsCallRepository.findByTradingSymbol(index);
				
				for (EarningsCall call : calls) {
					stockDownloader.calculateYields(call);
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
	*/
	
}
