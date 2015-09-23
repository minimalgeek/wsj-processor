package hu.farago.data.download.yahoo;

import hu.farago.data.api.ForexDataDownloader;
import hu.farago.data.api.dto.ForexData;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class YahooDataDownloader implements ForexDataDownloader {

	@Value("#{'${currency.pairs}'.split(',')}")
	private List<String> currencyPairs;

	@Override
	public ForexData getDataForSymbol(String symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ForexData getDataForSymbolBetweenDates(String symbol, Date from,
			Date to) {
		// TODO Auto-generated method stub
		return null;
	}

//	public List<HistoricalQuote> downloadHistoricalDailyDataForCurrencyPair(String currencyPair,
//			Calendar fromDate, Calendar toDate) throws IOException {
//		
//		String correctedYahooPair = currencyPair + "=X";
//		
////		Stock stock = YahooFinance.get(correctedYahooPair, fromDate, toDate, Interval.DAILY);
//		FxQuote fxQuote = YahooFinance.getFx(correctedYahooPair);
//		return null;
//	}
	
	
	
}
