package hu.farago.data.seekingalpha;


/**
 * Split the given plain text to sentences, then to words. It's
 * managed by Stanford CoreNLP project.
 * 
 * @author Balázs
 *
 */
//@Component
public class CoreNLPWordsProcessor {} /*implements WordProcessor {

	@Value("${nlp.annotators}")
	private String annotators;

	private StanfordCoreNLP coreNLP;

	public List<String> parseArticlePlainTextAndBuildMapOfWords(String article) {
		List<String> lemmas = lemmatize(article);

		List<String> words = Lists.newLinkedList();
		for (String lemma : lemmas) {
			if (org.apache.commons.lang3.StringUtils.isNotBlank(lemma)) {
				words.add(org.apache.commons.lang3.StringUtils.lowerCase(lemma));
			}
		}

		return words;
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

}*/
