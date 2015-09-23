package hu.farago.data.yahoo.download;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.fx.FxQuote;

@Component
public class YahooDataDownloader {

	@Value("#{'${currency.pairs}'.split(',')}")
	private List<String> currencyPairs;

	public List<HistoricalQuote> downloadHistoricalDailyDataForCurrencyPair(String currencyPair,
			Calendar fromDate, Calendar toDate) throws IOException {
		
		String correctedYahooPair = currencyPair + "=X";
		
//		Stock stock = YahooFinance.get(correctedYahooPair, fromDate, toDate, Interval.DAILY);
		FxQuote fxQuote = YahooFinance.getFx(correctedYahooPair);
		return null;
	}
	
}
