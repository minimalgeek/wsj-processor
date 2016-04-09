package hu.farago.data.model.entity.mongo;

import hu.farago.data.edgar.dto.EdgarXML;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "edgar_data")
public class EdgarData {
	
	@Id
	public BigInteger id;
	public String formURL;
	public String tradingSymbol;
	
	public EdgarXML edgarXML;
	
}
