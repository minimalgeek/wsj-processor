package hu.farago.yahoo.download;

import hu.farago.yahoo.dto.TickData;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;
import yahoofinance.quotes.stock.StockQuote;

import com.google.common.collect.Lists;

public class YahooCurrencPairDownloader {
	
	private PropertyLoader.Result properties;
	
	public YahooCurrencPairDownloader() throws IOException {
		properties  = new PropertyLoader().getPropValues();
	}

	public TickData getTickDataForCurrencyPair(String currencyPair) throws IOException {
		FxQuote fx = YahooFinance.getFx(currencyPair + "=X");
		Stock stock = YahooFinance.get(currencyPair + "=X");
		
		TickData data = new TickData();
		StockQuote quote = stock.getQuote();
		data.setHigh(quote.getDayHigh().doubleValue());
		data.setLow(quote.getDayLow().doubleValue());
		data.setOpen(quote.getOpen().doubleValue());
		data.setTime(new Date());
		data.setVolume(quote.getVolume());
		data.setLast(fx.getPrice().doubleValue());
		data.setCurrencyPair(currencyPair);

		return data;
	}

	public List<TickData> getAllTickData() throws IOException {
		List<TickData> returnList = Lists.newArrayList();
		
		for (String pair : properties.currencyPairs) {
			returnList.add(getTickDataForCurrencyPair(pair));
		}
		
		return returnList;
	}
	
	public PropertyLoader.Result getProperties() {
		return properties;
	}
	
}
