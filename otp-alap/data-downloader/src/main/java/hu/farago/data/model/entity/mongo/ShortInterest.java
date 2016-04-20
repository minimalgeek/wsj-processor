package hu.farago.data.model.entity.mongo;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "short_interest")
public class ShortInterest implements Serializable {

	private static final long serialVersionUID = -5233823788675604364L;

	@Id
	public BigInteger id;
	public String tradingSymbol;
	public DateTime settlementDate;
	public double shortInterest;
	public double avgDailyShareVolume;
	public double daysToCover;
	
	@Override
	public boolean equals(Object obj) {
		ShortInterest other = (ShortInterest) obj;
		
		EqualsBuilder eb = new EqualsBuilder();
		
		eb.append(this.tradingSymbol, other.tradingSymbol);
		eb.append(this.settlementDate, other.settlementDate);
		
		return eb.isEquals();
	}
}
