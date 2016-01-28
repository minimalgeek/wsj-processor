package hu.farago.data.seekingalpha;

import hu.farago.data.api.WordProcessor;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component("simpleWordProcessor")
public class SimpleWordProcessor implements WordProcessor{

	@Override
	public List<String> parseArticlePlainTextAndBuildMapOfWords(String article) {
		return lemmatize(article);
	}
	
	private List<String> lemmatize(String article) {
		List<String> retList = Lists.newLinkedList();
		
		String[] words = article.split("\\s+");
		for (int i = 0; i < words.length; i++) {
			retList.add(StringUtils.lowerCase(words[i].replaceAll("[^\\w]", "")));
		}
		
		return retList;
	}
	
}
