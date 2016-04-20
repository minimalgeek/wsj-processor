package hu.farago.data.nasdaq;

public enum Exchange {
	
	NASDAQ("nasdaq"), NYSE("nyse"), AMEX("amex");
	
	private String name;
	
	Exchange(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}