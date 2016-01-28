package hu.farago.data.model.entity.mongo;

import java.io.Serializable;
import java.math.BigInteger;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "insider_data")
public class InsiderData implements Serializable {

	private static final long serialVersionUID = 5800207901368724144L;

	@Id
	private BigInteger id;
	
	public BuySell type;
	public DateTime transactionDate;
	public DateTime acceptanceDate;
	public String issuerName;
	public String issuerTradingSymbol;
	public String reportingOwnerName;
	public OwnerRelationShip ownerRelationShip;
	public double transactionShares;
	public double pricePerShare;
	public double totalValue;
	public double sharesOwned;
	
	public enum BuySell {
		
		BUY("Buy"), SELL("Sell");
		
		private String name;
		
		private BuySell(String name) {
			this.name = name;
		}
		
		public static BuySell createByName(String name) {
			for (BuySell temp : values()) {
				if (temp.name.equals(name)) {
					return temp;
				}
			}
			return null;
		}
	}
	
	public enum OwnerRelationShip {
		
		DIRECTOR("director"), 
		DIRECTOR_OFFICER("director officer"), 
		OFFICER("officer"),
		OWNER("owner"),
		OTHER("other"),
		TENPERCENTOWNER("10% owner");
		
		private String name;
		
		private OwnerRelationShip(String name) {
			this.name = name;
		}
		
		public static OwnerRelationShip createByName(String name) {
			for (OwnerRelationShip temp : values()) {
				if (temp.name.equals(name)) {
					return temp;
				}
			}
			
			return null;
		}
	}
}
