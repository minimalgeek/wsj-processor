package hu.farago.data.api;

import java.util.List;

public interface WordProcessor {

	public List<String> parseArticlePlainTextAndBuildMapOfWords(String text);
	
}
