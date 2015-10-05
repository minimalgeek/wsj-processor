package hu.farago.wsj.parse.wordprocess;

import hu.farago.wsj.model.entity.mongo.ArticleCollection;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

/**
 * Split the given article's plain text to sentences, then to words. It's
 * managed by Stanford CoreNLP project.
 * 
 * @author Balázs
 *
 */
@Service
public class CoreNLPWordsProcessor {

	@Value("${wsj.receiver.annotators}")
	private String annotators;

	private StanfordCoreNLP coreNLP;

	public void parseArticlePlainTextAndBuildMapOfWords(ArticleCollection article) {
		List<String> lemmas = lemmatize(article.getPlainText());

		article.setArticleWords(new HashSet<String>());
		for (String lemma : lemmas) {
			if (org.apache.commons.lang3.StringUtils.isNotBlank(lemma)) {
				article.getArticleWords().add(lemma);
			}
		}

	}

	private List<String> lemmatize(String documentText) {
		List<String> lemmas = new LinkedList<String>();

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(documentText);

		// run all Annotators on this text
		this.coreNLP.annotate(document);

		// Iterate over all of the sentences found
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			// Iterate over all tokens in a sentence
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// Retrieve and add the lemma for each word into the list of
				// lemmas
				String lemma = token.get(LemmaAnnotation.class);
				String partOfSpeech = token.get(PartOfSpeechAnnotation.class);
				if (StringUtils.isAlpha(lemma)
						|| StringUtils.isAlpha(partOfSpeech)) {
					lemmas.add(lemma);
				}
			}
		}

		return lemmas;
	}

	private void buildCoreNLPIfNull() {
		if (coreNLP == null) {
			Properties props = new Properties();
			props.setProperty("annotators", annotators);
			coreNLP = new StanfordCoreNLP(props);
		}
	}

	@PostConstruct
	public void postConstruct() {
		buildCoreNLPIfNull();
	}

}
