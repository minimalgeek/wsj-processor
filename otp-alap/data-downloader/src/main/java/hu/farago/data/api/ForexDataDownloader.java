package hu.farago.data.api;

import hu.farago.data.api.dto.ForexData;

import org.joda.time.DateTime;

public interface ForexDataDownloader {

	public ForexData getDataForSymbol(String symbol);
	
	public ForexData getDataForSymbolBetweenDates(String symbol, DateTime from, DateTime to);
	
}
