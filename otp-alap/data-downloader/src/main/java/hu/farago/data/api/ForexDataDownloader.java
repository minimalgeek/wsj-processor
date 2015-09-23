package hu.farago.data.api;

import hu.farago.data.api.dto.ForexData;

import java.util.Date;

public interface ForexDataDownloader {

	public ForexData getDataForSymbol(String symbol);
	
	public ForexData getDataForSymbolBetweenDates(String symbol, Date from, Date to);
	
}
