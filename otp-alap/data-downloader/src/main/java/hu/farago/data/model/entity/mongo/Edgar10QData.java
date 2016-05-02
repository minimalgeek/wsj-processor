package hu.farago.data.model.entity.mongo;

import java.math.BigInteger;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "edgar_10q_data")
public class Edgar10QData {

	@Id
	public BigInteger id;
	public String formURL;
	public String tradingSymbol;
	public DateTime reportDate;
	
	public Long formLength;
	public Long footnoteLength;
	
}
