package hu.farago.data.model.entity.mongo;

import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ipo_activity")
public class IPOActivity {

	@Id
	public BigInteger id;
	public String tradingSymbol;
	public String market;
	public double price;
	public DateTime datePriced;
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		
		builder.append(tradingSymbol);
		builder.append(market);
		builder.append(price);
		builder.append(datePriced);
		
		return builder.build();
	}
	
}
