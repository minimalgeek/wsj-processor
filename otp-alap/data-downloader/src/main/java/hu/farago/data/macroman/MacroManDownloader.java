package hu.farago.data.macroman;

import hu.farago.data.api.UrlBasedDownloader;
import hu.farago.data.api.WordProcessor;
import hu.farago.data.model.dao.mongo.MacroManRepository;
import hu.farago.data.model.entity.mongo.MacroMan;
import hu.farago.data.model.entity.mongo.embedded.Contributor;
import hu.farago.data.model.entity.mongo.embedded.ToneWithWords;
import hu.farago.data.seekingalpha.ToneCalculator;
import hu.farago.data.semantic.EnglishStemmerWithPatternRemover;
import hu.farago.data.utils.DateTimeUtils;
import hu.farago.data.utils.URLUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import edu.ucla.sspace.text.IteratorFactory;

@Component
public class MacroManDownloader extends UrlBasedDownloader<MacroMan> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MacroManDownloader.class);

	@Value("${macroman.urlBase}")
	private String urlBase;
	@Value("${macroman.urlEnd}")
	private String urlEnd;
	@Value("${macroman.start.year}")
	private int startYear;
	@Value("${macroman.start.month}")
	private int startMonth;
	@Value("${macroman.end.year}")
	private int endYear;
	@Value("${macroman.end.month}")
	private int endMonth;

	@Autowired
	private ToneCalculator calculator;

	@Autowired
	private WordProcessor simpleWordProcessor;
	
	@Autowired
	private MacroManRepository repository;

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
	
	@Override
	protected MongoRepository<MacroMan, BigInteger> getRepository() {
		return repository;
	}

	@Override
	protected List<String> buildUrls() throws Exception {
		List<String> urls = Lists.newArrayList();
		for (int year = startYear; year <= endYear; year++) {
			for (int month = 1; month <= 12; month++) {
				if (year == startYear && month < startMonth) {
					continue;
				}
				if (year == endYear && month > endMonth) {
					break;
				}

				String url = urlBase + year + "_"
						+ StringUtils.leftPad("" + month, 2, '0') + urlEnd;

				try {
					Document doc = URLUtils.getDocumentContentOfURL(url);
					for (Element link : doc.getElementsByClass("posts").first()
							.select("li a")) {
						urls.add(link.attr("href"));
					}
				} catch (Exception e) {
					LOGGER.error("Error at: " + url + "\n" + e.getMessage());
				}
			}
		}
		return urls;
	}

	@Override
	protected MacroMan buildDocument(Document doc) {
		MacroMan macro = new MacroMan();

		String title = doc.getElementsByClass("published").first()
				.attr("title");
		DateTime publishDate = DateTimeUtils.parseToYYYYMMDD_HHmmss_ZONE(title)
				.withZone(DateTimeZone.UTC);
		macro.publicationDate = publishDate;

		for (Element comment : doc.getElementsByClass("comment_body")) {
			if (comment.getElementsByTag("p").isEmpty()) {
				continue;
			}
			String commentText = comment.getElementsByTag("p").first().text();
			String userName = comment.getElementsByClass("comment_name")
					.first().text();

			Contributor contrib = new Contributor();
			contrib.name = userName;
			contrib.tone = buildTone(commentText);

			macro.contributors.add(contrib);
		}

		String docText = doc.getElementsByTag("article").first().text();
		macro.tone = buildTone(docText);

		return macro;
	}

	private ToneWithWords buildTone(String commentText) {
		ToneWithWords tww = calculator.getToneWithWords(
				simpleWordProcessor.parseArticlePlainTextAndBuildMapOfWords(commentText));
		try {
			tww.stemmedText = tokenizeAndStemTextFile(commentText);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return tww;
	}
	
	private static final EnglishStemmerWithPatternRemover stemmer = new EnglishStemmerWithPatternRemover();
	
	private List<String> tokenizeAndStemTextFile(String text) throws IOException {
		List<String> tokens = Lists.newArrayList(IteratorFactory.tokenize(text));
		List<String> stemmedTokens = Lists.newArrayList();
		for (String token : tokens) {
			String tmp = stemmer.stem(token);
			if (StringUtils.isNotEmpty(tmp)) {
				stemmedTokens.add(tmp);
			}
		}
		return stemmedTokens;
	}	
}
