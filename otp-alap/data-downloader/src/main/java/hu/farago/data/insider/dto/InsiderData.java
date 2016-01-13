package hu.farago.data.insider.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

public class InsiderData implements Serializable {

	private static final long serialVersionUID = 5800207901368724144L;

	public BuySell type;
	public DateTime transactionDate;
	public DateTime acceptanceDate;
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
