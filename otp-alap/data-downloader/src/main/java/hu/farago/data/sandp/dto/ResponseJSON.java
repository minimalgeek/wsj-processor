package hu.farago.data.sandp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseJSON {

	public boolean subscriptionStatus;
	public boolean success; 
	
	@JsonProperty("widget_data")
	public List<CompanyJSON> companies;
	
}
