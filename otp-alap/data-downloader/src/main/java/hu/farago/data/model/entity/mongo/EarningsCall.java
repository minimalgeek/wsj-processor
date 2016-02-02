package hu.farago.data.model.entity.mongo;

import hu.farago.data.seekingalpha.dto.HTone;
import hu.farago.data.seekingalpha.dto.Tone;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "earnings_call")
public class EarningsCall implements Serializable {
	
	private static final long serialVersionUID = -7825655570337352878L;
	
	@Id
	public BigInteger id;
	public String tradingSymbol;
	public String rawText;
	public DateTime publishDate;
	public List<String> words;
	
	@Field("tone")
	public Tone tone;
	@Field("h_tone")
	public HTone hTone;
	
}
