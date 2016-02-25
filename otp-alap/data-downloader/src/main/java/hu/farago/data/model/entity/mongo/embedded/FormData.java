package hu.farago.data.model.entity.mongo.embedded;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class FormData {

	public String titleOfSecurity;
	public DateTime transactionDate;
	
	public int code01;
	public String code02;
	public int code03;
	public String code04;
	
	public double amountOfAcquired;
	public String acquired; // A or D
	public double priceOfAcquired;
	
	public double sharesOwned;
	public String ownershipForm; // D or I
	
	
}
