package hu.farago.data.download.service;

import hu.farago.data.api.ForexDataDownloader;
import hu.farago.data.api.dto.ForexData;
import hu.farago.data.api.dto.HistoricalForexData;
import hu.farago.data.model.dao.sql.ForexRepository;
import hu.farago.data.model.entity.sql.Forex;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForexDataDownloaderService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ForexDataDownloaderService.class);

	@Value("#{'${currency.pairs}'.split(',')}")
	private List<String> currencyPairs;
	
	@Autowired
	private ForexDataDownloader stooqDataDownloader;
	
	@Autowired
	private ForexRepository forexRepository;
	
	@RequestMapping(value = "/downloadAll", method = RequestMethod.GET)
	public void downloadAll() {
		
		LOGGER.info("downloadAll");
		
		forexRepository.deleteAll();
		
		for (String pair : currencyPairs) {
			ForexData allData = stooqDataDownloader.getDataForSymbol(pair);
			
			for (HistoricalForexData histData : allData.getHistoricalForexDataList()) {
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
		
		LOGGER.info("downloadAll ended");
	}
	
	@Scheduled(cron = "0 0/60 * * * ?")
	@RequestMapping(value = "/downloadMissing", method = RequestMethod.GET)
	public void downloadMissing() {
		
		LOGGER.info("downloadMissing");
		
		for (String pair : currencyPairs) {
			Date latestDateForPair = forexRepository.findLatestDateForSymbol(StringUtils.upperCase(pair));
			Date fromDateForPair = DateUtils.addDays(latestDateForPair, 1);
			
			ForexData allData = stooqDataDownloader.getDataForSymbolBetweenDates(pair, fromDateForPair, new Date());
			
			for (HistoricalForexData histData : allData.getHistoricalForexDataList()) {
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
		
		LOGGER.info("downloadAll ended");
	}
	
}