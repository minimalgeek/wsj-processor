package hu.farago.data.model.entity.mongo;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "henry_words")
public class HenryWords {

	@Id
	public BigInteger id;
	@Field("Word")
	public String word;
	@Field("Positiv")
	public String positiv;
	@Field("Negativ")
	public String negativ;
	
	public boolean isPositive() {
		return StringUtils.isNotBlank(positiv);
	}
	
	public boolean isNegative() {
		return StringUtils.isNotBlank(negativ);
	}
	
}
