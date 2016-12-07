package hu.farago.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import hu.farago.data.model.dao.mongo.EarningsCallRepository;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.seekingalpha.ProcessFirstNArticleParameter;
import hu.farago.data.seekingalpha.SeekingAlphaDownloader;
import hu.farago.data.seekingalpha.YahooStockDownloader;
import hu.farago.data.seekingalpha.bloomberg.EarningsCallFileImporter;
import hu.farago.data.zacks.ZacksECDateManager;

@RestController
public class SeekingAlphaDownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SeekingAlphaDownloadService.class);

	@Autowired
	private SeekingAlphaDownloader seekingAlphaDownloader;
	@Autowired
	private EarningsCallFileImporter fileImporter;
	@Autowired
	private EarningsCallRepository earningsCallRepository;
	// @Autowired
	// private InsiderDataRepository insiderDataRepo;
	// @Autowired
	// private EarningsCallAndInsiderDataAggregator aggregator;
	@Autowired
	private YahooStockDownloader stockDownloader;
	@Autowired
	private ZacksECDateManager manager;

	@RequestMapping(value = "/collectEarningsCalls", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
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

	@RequestMapping(value = "/importAllFiles", method = RequestMethod.GET)
	public void importAllFiles() {
		LOGGER.info("importAllFiles");
		fileImporter.importAll();
	}

	@RequestMapping(value = "/collectEarningsCallsFor/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public String collectEarningsCallsFor(@PathVariable("id") String index) {
		LOGGER.info("collectEarningsCallsFor");

		StringBuilder ret = new StringBuilder();

		try {
			List<EarningsCall> list = seekingAlphaDownloader.collectAllDataForIndex(index);

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

	@RequestMapping(value = "/processQAndAAndAddStockData", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
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

	// http://localhost:8082/data-downloader/collectLastNTranscripts/4
	@RequestMapping(value = "/collectLastNTranscripts/{nrOfTranscripts}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<String> collectLastNTranscripts(@PathVariable("nrOfTranscripts") int numberOfTranscriptsNeeded) {
		LOGGER.info("collectLastNTranscripts");

		List<String> ret = new ArrayList<>();

		for (String indexName : seekingAlphaDownloader.getIndexes()) {
			try {
				List<EarningsCall> calls = seekingAlphaDownloader.collectLatestNForIndex(
						new ProcessFirstNArticleParameter(indexName, numberOfTranscriptsNeeded));

				for (EarningsCall call : calls) {
					if (earningsCallRepository.findByUrl(call.url) == null) {
						LOGGER.info("New earnings call found: " + call.url);
						earningsCallRepository.save(call);
						ret.add(call.url);
					}
				}

			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		return ret;
	}

	@RequestMapping(value = "/lookForTranscripts", method = RequestMethod.GET)
	public void lookForTranscripts() {
		LOGGER.info("lookForTranscripts");
		manager.lookForTranscripts();
	}

	// every day at midnight
	@Scheduled(cron = "0 0 12 * * ?")
	public void lookForTranscriptsScheduled() {
		lookForTranscripts();
	}

	/*
	 * @RequestMapping(value = "/appendInsiderDataToEarningsCall", method =
	 * RequestMethod.GET) public void appendInsiderDataToEarningsCall() {
	 * LOGGER.info("appendInsiderDataToEarningsCall");
	 * 
	 * for (String index : seekingAlphaDownloader.getIndexes()) {
	 * List<EarningsCall> calls =
	 * earningsCallRepository.findByTradingSymbol(index); calls.sort(new
	 * Comparator<EarningsCall>() {
	 * 
	 * @Override public int compare(EarningsCall o1, EarningsCall o2) { return
	 * o1.publishDate.compareTo(o2.publishDate); }
	 * 
	 * }); List<InsiderData> insiderDataList =
	 * insiderDataRepo.findByIssuerTradingSymbol(index);
	 * 
	 * EarningsCall previousCall = null;
	 * 
	 * for (EarningsCall call : calls) { if (call.tone != null && previousCall
	 * != null) { aggregator.processCall(call, previousCall, insiderDataList); }
	 * 
	 * previousCall = call; }
	 * 
	 * earningsCallRepository.save(calls); LOGGER.info(index + " processed"); }
	 * }
	 */
}
