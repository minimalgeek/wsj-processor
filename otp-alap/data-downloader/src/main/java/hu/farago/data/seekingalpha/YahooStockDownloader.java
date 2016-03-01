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
			Thread.sleep(200);
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
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public void calculateYields(EarningsCall call) {
		/*
		if (call.stockData != null) {
			Ordering<StockData> o = new Ordering<StockData>() {
			    @Override
			    public int compare(StockData left, StockData right) {
			        return left.dateTime.compareTo(right.dateTime);
			    }
			};
			
			StockData minData = o.min(call.stockData);
			
			for (StockData tempStockData : call.stockData) {
				Duration duration = new Duration(minData.dateTime, tempStockData.dateTime);
				
				if (duration.getStandardDays()>=1 && duration.getStandardDays()<=3) {
					call.oneDayYieldPerc = (float) (tempStockData.closePrice / minData.closePrice);
				}
				
				if (duration.getStandardDays()>=4 && duration.getStandardDays()<=6) {
					call.fiveDayYieldPerc = (float) (tempStockData.closePrice / minData.closePrice);
				}
				
				if (duration.getStandardDays()>=9 && duration.getStandardDays()<=11) {
					call.tenDayYieldPerc = (float) (tempStockData.closePrice / minData.closePrice);
				}
			}
			
			//if (call.oneDayYieldPerc == 0f || call.fiveDayYieldPerc == 0f || call.tenDayYieldPerc == 0f)
			
		}
		*/
	}

}
