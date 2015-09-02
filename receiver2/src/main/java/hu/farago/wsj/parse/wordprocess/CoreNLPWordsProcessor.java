package hu.farago.wsj.parse.wordprocess;

import hu.farago.wsj.model.entity.Article;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service("coreNLP")
public class CoreNLPWordsProcessor extends WordsProcessor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CoreNLPWordsProcessor.class);

	@Value("${wsj.receiver.annotators}")
	private String annotators;

	private StanfordCoreNLP coreNLP;

	@Override
	protected void doBuild(Article existingArticle) throws Exception {
		List<String> lemmas = lemmatize(existingArticle.getPlainText());

		for (String lemma : lemmas) {
			if (org.apache.commons.lang3.StringUtils.isNotBlank(lemma)) {
				addWordToArticle(existingArticle, lemma);
			}
		}

	}

	public List<String> lemmatize(String documentText) throws Exception {
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

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

}
