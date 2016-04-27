package hu.farago.data.model.entity.mongo;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "edgar_10k_data")
public class Edgar10KData {

	@Id
	public BigInteger id;
	public String formURL;
	public String tradingSymbol;
	
	public Long formLength;
	public Long footnoteLength;
	
}
