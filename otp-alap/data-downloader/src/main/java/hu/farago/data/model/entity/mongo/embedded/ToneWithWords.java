package hu.farago.data.model.entity.mongo.embedded;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ToneWithWords {

	public int words;
	public int positiveWords;
	public int negativeWords;
	
}
