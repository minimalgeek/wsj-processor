package hu.farago.data.download.service;

import hu.farago.data.api.ForexDataDownloader;
import hu.farago.data.api.dto.ForexData;
import hu.farago.data.api.dto.HistoricalForexData;
import hu.farago.data.download.yahoo.YahooCurrencyPairDownloader;
import hu.farago.data.model.dao.sql.ForexRepository;
import hu.farago.data.model.entity.sql.Forex;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class ForexDataDownloaderService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ForexDataDownloaderService.class);

	@Value("#{'${currency.pairs}'.split(',')}")
	private List<String> currencyPairs;
	
	@Autowired
	private ForexDataDownloader stooqDataDownloader;
	
	@Autowired
	private YahooCurrencyPairDownloader yahooCurrencPairDownloader;
	
	@Autowired
	private ForexRepository forexRepository;
	
	@RequestMapping(value = "/downloadAll", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<String> downloadAll() {
		
		LOGGER.info("downloadAll");
		
		forexRepository.deleteAll();
		List<String> downloadedPairs = Lists.newArrayList();
		
		for (String pair : currencyPairs) {
			ForexData allData = stooqDataDownloader.getDataForSymbol(pair);
			
			saveForexDataFromList(allData);
			
			downloadedPairs.add(pair);
		}
		
		return downloadedPairs;
	}
	
	@RequestMapping(value = "/downloadMissing", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<String> downloadMissing() {
		
		LOGGER.info("downloadMissing");
		
		List<String> downloadedPairs = Lists.newArrayList();
		
		for (String pair : currencyPairs) {
			Date latestDateForPair = forexRepository.findLatestDateForSymbol(StringUtils.upperCase(pair));
			if (latestDateForPair == null) {
				latestDateForPair = new Date();
			}
			
			ForexData allData = stooqDataDownloader.getDataForSymbolBetweenDates(pair, latestDateForPair, new Date());
			
			saveForexDataFromList(allData);
			downloadedPairs.add(pair);
		}
		
		return downloadedPairs;
	}
	
	@Scheduled(cron = "0 0/60 * * * ?")
	public void downloadMissingScheduled() {
		downloadMissing();
	}
	
	@Scheduled(fixedDelay = 5000)
	public void downloadTickDataScheduled() {
		for (String pair : currencyPairs) {
			LOGGER.info("get tick data for " + pair);
			ForexData data = yahooCurrencPairDownloader.getTickDataForSymbol(pair);
			saveForexDataFromList(data);
		}
	}

	private void saveForexDataFromList(ForexData allData) {
		for (HistoricalForexData histData : allData.getHistoricalForexDataList()) {
			// sometimes Stooq send an intraday update with wrong CLOSE value, so we
			// delete data from this date!
			
			forexRepository.deleteByDateAndSymbol(histData.getDate(), allData.getSymbol());
			
			Forex forex = new Forex();
			forex.setSymbol(StringUtils.upperCase(allData.getSymbol()));
			forex.setTickDate(histData.getDate());
			forex.setOpen(histData.getOpen());
			forex.setHigh(histData.getHigh());
			forex.setLow(histData.getLow());
			forex.setClose(histData.getClose());
			
			forexRepository.save(forex);
		}
	}
	
}
