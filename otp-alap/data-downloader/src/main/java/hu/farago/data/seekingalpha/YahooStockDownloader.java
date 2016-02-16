package hu.farago.data.seekingalpha;

import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.seekingalpha.dto.StockData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@Component
public class YahooStockDownloader  {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(YahooStockDownloader.class);
	
	public void addStockData(EarningsCall call) {
		try {
			Stock stock = YahooFinance.get(call.tradingSymbol, 
					call.publishDate.toCalendar(Locale.US), 
					call.publishDate.plusDays(30).toCalendar(Locale.US), Interval.DAILY);
			
			List<HistoricalQuote> quotes = stock.getHistory();
			
			if (call.stockData == null) {
				call.stockData = new ArrayList<StockData>();
			}
			
			for (HistoricalQuote quote : quotes) {
				call.stockData.add(new StockData(new DateTime(quote.getDate()), quote.getClose().doubleValue()));
			}
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
