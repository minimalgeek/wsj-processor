package hu.farago.data.sandp.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyJSON {

	@JsonProperty("INDEX_ID")
	public Integer indexId;
	
	@JsonProperty("CLOSE_AS_OF_DATE")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM-dd-yyyy", timezone="UTC")
	public Date closeAsOfDate;
	
	@JsonProperty("CURRENT_COMPANY_NAME")
	public String currentCompanyName;
	
	@JsonProperty("CURRENT_TICKER")
	public String currentTicker;
	
	@JsonProperty("INDEX_KEY")
	public String indexKey;
	
	@JsonProperty("INDEX_NAME")
	public String indexName;
	
	@JsonProperty("LAST_UPDATED") // Mar 29, 2016 10:00 AM
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MMM dd, yyyy HH:mm a z", timezone="UTC")
	public String lastUpdated;
	
	@JsonProperty("EVENT_NAME")
	public String eventName;
	
}
