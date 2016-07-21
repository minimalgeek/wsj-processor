package hu.farago.data.model.entity.mongo;

import hu.farago.data.seekingalpha.dto.HTone;
import hu.farago.data.seekingalpha.dto.StockData;
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
	public String url;
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
	
	public double sumOfSharesOwnedBeforePublishDate;
	public double sumOfBuyTransactionShares;
	public double sumOfSellTransactionShares;
}
