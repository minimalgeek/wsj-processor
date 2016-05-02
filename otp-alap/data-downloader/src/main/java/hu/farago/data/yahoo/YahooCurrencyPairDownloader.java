package hu.farago.data.yahoo;

import hu.farago.data.api.dto.ForexData;
import hu.farago.data.api.dto.HistoricalForexData;

import java.io.IOException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;

import com.google.common.collect.Lists;

@Component
public class YahooCurrencyPairDownloader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(YahooCurrencyPairDownloader.class);

	public ForexData getTickDataForSymbol(String symbol) {
		try {
			ForexData forexData = new ForexData(symbol);
			FxQuote fx = YahooFinance.getFx(symbol + "=X");
			forexData.setHistoricalForexDataList(Lists
					.newArrayList(new HistoricalForexData(new DateTime(), fx
							.getPrice().doubleValue(), null, null, null)));

			return forexData;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

}
