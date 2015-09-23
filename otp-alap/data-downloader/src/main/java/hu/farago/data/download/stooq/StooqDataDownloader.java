package hu.farago.data.download.stooq;

import hu.farago.data.api.ForexDataDownloader;
import hu.farago.data.api.dto.ForexData;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StooqDataDownloader implements ForexDataDownloader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StooqDataDownloader.class);

	private static final String URL_PREFIX = "http://stooq.com/q/d/l/?s=";
	private static final String URL_POSTFIX = "&i=d";

	// http://stooq.com/q/d/l/?s=eurusd&i=d

	private static final String URL_RANGE_DATE_FROM = "&d1=";
	private static final String URL_RANGE_DATE_TO = "&d2=";

	// http://stooq.com/q/d/l/?s=eurusd&d1=19710101&d2=20150923&i=d

	private static final SimpleDateFormat stooqDateFormat = new SimpleDateFormat(
			"yyyyMMdd");

	@Autowired
	private CsvFileToHistoricalDataConverter historicalDataConverter;
	
	@Override
	public ForexData getDataForSymbol(String symbol) {
		return getDataForSymbolBetweenDates(symbol, null, null);
	}

	@Override
	public ForexData getDataForSymbolBetweenDates(String symbol, Date from,
			Date to) {
		ForexData forexData = new ForexData(symbol);

		try {
			URL urlToQuery = new URL(buildUrl(symbol, from, to));
			File tempFile = File.createTempFile(
					"symbol-" + stooqDateFormat.format(new Date()), ".tmp");
			FileUtils.copyURLToFile(urlToQuery, tempFile);
			
			forexData.setHistoricalForexDataList(historicalDataConverter.convert(tempFile));
		} catch (IOException | ParseException ex) {
			LOGGER.error(ex.getMessage(), ex);
		}

		return forexData;
	}

	private String buildUrl(String symbol, Date from, Date to) {
		StringBuilder urlBuilder = new StringBuilder(URL_PREFIX);
		urlBuilder.append(StringUtils.lowerCase(symbol));
		if (from != null && to != null) {
			// query within a range
			urlBuilder.append(URL_RANGE_DATE_FROM);
			urlBuilder.append(stooqDateFormat.format(from));
			urlBuilder.append(URL_RANGE_DATE_TO);
			urlBuilder.append(stooqDateFormat.format(to));
		}
		urlBuilder.append(URL_POSTFIX);
		String url = urlBuilder.toString();
		return url;
	}

	protected Date getNewDate(int year, int month, int day) {
		Calendar date = Calendar.getInstance();
		date.set(year, month, day);
		return date.getTime();
	}

}
