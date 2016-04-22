package hu.farago.data.model.entity.mongo;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "zacks_earnings_call_dates_ii")
public class ZacksEarningsCallDates2 implements Serializable {

	private static final long serialVersionUID = -5487447385592478445L;

	@Id
	public BigInteger id;
	
	public DateTime nextReportDate;
	public String tradingSymbol;
	//public List<DateTime> seekingAlphaCheckDate = Lists.newArrayList();
	
	//public BigInteger foundEarningsCallId;
	
	public DateTime nextReportDateInLocal() {
		return nextReportDate.withZone(DateTimeZone.getDefault());
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append(tradingSymbol)
			.append(nextReportDate)
			.build();
	}
	
}
