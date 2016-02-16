package hu.farago.data.seekingalpha.dto;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StockData {
		
	public StockData(DateTime dateTime, double closePrice) {
		this.dateTime = dateTime;
		this.closePrice = closePrice;
	}
	
	public DateTime dateTime;
	public double closePrice;
	
}
