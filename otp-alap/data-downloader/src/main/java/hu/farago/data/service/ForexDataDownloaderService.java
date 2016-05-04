package hu.farago.data.service;

import hu.farago.data.api.ForexDataDownloader;
import hu.farago.data.api.dto.ForexData;
import hu.farago.data.api.dto.HistoricalForexData;
import hu.farago.data.model.dao.mongo.ForexRepository;
import hu.farago.data.model.entity.mongo.Forex;
import hu.farago.data.model.entity.mongo.QForex;
import hu.farago.data.yahoo.YahooCurrencyPairDownloader;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
	@Value("${currency.shouldRun}")
	private boolean shouldRun;

	@Autowired
	private ForexDataDownloader stooqDataDownloader;

	@Autowired
	private YahooCurrencyPairDownloader yahooCurrencPairDownloader;

	@Autowired
	private ForexRepository forexRepository;

	@RequestMapping(value = "/downloadAll", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
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

	@RequestMapping(value = "/downloadMissing", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> downloadMissing() {

		LOGGER.info("downloadMissing");

		List<String> downloadedPairs = Lists.newArrayList();

		for (String pair : currencyPairs) {
			Forex latestForex = forexRepository.findFirstBySymbolOrderByTickDateDesc(pair);
			DateTime latestForexDateTime = null;
			if (latestForex == null) {
				latestForexDateTime = DateTime.now(DateTimeZone.UTC);
			} else {
				latestForexDateTime = latestForex.tickDate.withZone(DateTimeZone.UTC);
			}

			ForexData allData = stooqDataDownloader
					.getDataForSymbolBetweenDates(pair, latestForexDateTime,
							DateTime.now(DateTimeZone.UTC));

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
		if (shouldRun) {
			for (String pair : currencyPairs) {
				LOGGER.info("Get tick data for " + pair);
				ForexData data = yahooCurrencPairDownloader
						.getTickDataForSymbol(pair);
				saveForexDataFromList(data);
			}
		}
	}

	private void saveForexDataFromList(ForexData allData) {
		for (HistoricalForexData histData : allData
				.getHistoricalForexDataList()) {
			// sometimes Stooq send an intraday update with wrong CLOSE value,
			// so we delete data from this date!

			QForex forexPredicate = QForex.forex;
			Iterable<Forex> foundForexes = forexRepository
					.findAll(forexPredicate.symbol.eq(allData.getSymbol()).and(
							forexPredicate.tickDate.eq(histData.getDate())));
			forexRepository.delete(foundForexes);

			Forex forex = new Forex();
			forex.symbol = StringUtils.upperCase(allData.getSymbol());
			forex.tickDate = histData.getDate();
			forex.open = histData.getOpen();
			forex.high = histData.getHigh();
			forex.low = histData.getLow();
			forex.close = histData.getClose();
			
			forexRepository.save(forex);
		}
		
		LOGGER.info(allData.getSymbol() + " saved, with " + allData.getHistoricalForexDataList().size() + " rows");
	}

}
