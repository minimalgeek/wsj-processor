package hu.farago.data.model.entity.mongo.embedded;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;

@Document
public class ToneWithWords {

	public List<String> stemmedText = Lists.newLinkedList();
	public int words;
	public int positiveWords;
	public int negativeWords;
	
}
