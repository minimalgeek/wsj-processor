package hu.farago.data.model.entity.mongo;

import hu.farago.data.seekingalpha.dto.HTone;

import java.io.Serializable;
import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "oil_report")
public class OilReport  extends UrlBasedEntity implements Serializable {

	private static final long serialVersionUID = -1567361534615025091L;

	@Id
	public BigInteger id;
	
	@Field("highlights_tone")
	public HTone highlightsTone;
	
	@Field("overview_tone")
	public HTone overviewTone;
	
	@Field("demand_tone")
	public HTone demandTone;
	
	@Field("supply_tone")
	public HTone supplyTone;
	
	@Field("oecd_stocks_tone")
	public HTone oecdStocksTone;
	
	@Field("prices_tone")
	public HTone pricesTone;
	
	@Field("refining_tone")
	public HTone refiningTone;
	
	@Field("sum_total_tone")
	public HTone sumTotalTone;
	
}
