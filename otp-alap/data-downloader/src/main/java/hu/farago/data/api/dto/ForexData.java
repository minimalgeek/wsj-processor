package hu.farago.data.api.dto;

import java.util.List;

public class ForexData {

	private String symbol;
	private List<HistoricalForexData> historicalForexDataList;
	
	public ForexData(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public List<HistoricalForexData> getHistoricalForexDataList() {
		return historicalForexDataList;
	}

	public void setHistoricalForexDataList(
			List<HistoricalForexData> historicalForexDataList) {
		this.historicalForexDataList = historicalForexDataList;
	}

}
