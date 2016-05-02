package hu.farago.data.model.entity.mongo;

import java.math.BigInteger;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mysema.query.annotations.QueryEntity;

@Document(collection = "forex")
@QueryEntity
public class Forex {

	@Id
	public BigInteger id;
	public String symbol;
	public DateTime tickDate;
	
	public Double open;
	public Double high;
	public Double low;
	public Double close;
	public Double volume;
	
	public Forex(String symbol, DateTime tickDate, Double open, Double high,
			Double low, Double close, Double volume) {
		super();
		this.symbol = symbol;
		this.tickDate = tickDate;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	public Forex() {
	}

	
	
}
