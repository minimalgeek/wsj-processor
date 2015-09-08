package hu.farago.wsj.parse.wordprocess;

import hu.farago.wsj.model.entity.sql.Article;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("regex")
public class RegexWordsProcessor extends WordsProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegexWordsProcessor.class);
	
	private Pattern pattern = Pattern.compile("([a-zA-Z_]+-?[a-zA-Z_]+('[a-rt-zA-RT-Z]{1,2})?)|(i|I|a|A)");

	@Override
	protected void doBuild(Article existingArticle) throws Exception {		
		Matcher matcher = pattern.matcher(existingArticle.getPlainText());
		
		while (matcher.find()) {
			String str = matcher.group();
			if (StringUtils.isNotBlank(str)) {
				addWordToArticle(existingArticle, str);
			}	
		}
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

}
