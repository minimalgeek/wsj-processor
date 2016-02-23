package hu.farago.mongo.model.entity.mongo;

import hu.farago.mongo.model.entity.mongo.composit.HTone;
import hu.farago.mongo.model.entity.mongo.composit.StockData;
import hu.farago.mongo.model.entity.mongo.composit.Tone;

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
	
	public String qAndAText;
	public List<String> qAndAWords;
	
	@Field("q_and_a_tone")
	public Tone qAndATone;
	@Field("q_and_a_h_tone")
	public HTone qAndAHTone;
	
	public List<StockData> stockData;
	
	public float oneDayYieldPerc;
	public float fiveDayYieldPerc;
	public float tenDayYieldPerc;
}