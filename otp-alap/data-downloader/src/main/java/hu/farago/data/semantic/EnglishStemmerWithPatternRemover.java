package hu.farago.data.semantic;

import org.apache.commons.lang3.StringUtils;

import edu.ucla.sspace.text.EnglishStemmer;

public class EnglishStemmerWithPatternRemover extends EnglishStemmer {

	@Override
	public String stem(String token) {
		String stemmed = super.stem(token);
		return StringUtils.removePattern(StringUtils.lowerCase(stemmed), "[^\\w]");
	}
	
}
