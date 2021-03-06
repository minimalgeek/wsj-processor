package hu.farago.data.seekingalpha;

import hu.farago.data.api.DataDownloader;
import hu.farago.data.api.WordProcessor;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.model.entity.mongo.AutomaticServiceError.AutomaticService;
import hu.farago.data.utils.AutomaticServiceErrorUtils;
import hu.farago.data.utils.DateTimeUtils;
import hu.farago.data.utils.URLUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class SeekingAlphaDownloader extends DataDownloader<EarningsCall> {

	public static final String QUESTION_AND_ANSWER = "(?i)question-and-answer";
	public static final String QUESTION_AND_ANSWER_2 = "(?i)question and answer";
	public static final String COPYRIGHT_POLICY = "(?i)copyright policy";
	public static final String EARNINGS_CALL_TRANSCRIPT = "earnings call transcript";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SeekingAlphaDownloader.class);
		
	@Value("${seekingalpha.filePath}")
	private String filePath;
	@Value("${seekingalpha.articleUrlBase}")
	private String articleUrlBase;
	@Value("${seekingalpha.urlBase}")
	private String urlBase;
	@Value("${seekingalpha.urlMiddle}")
	private String urlMiddle;
	
	@Autowired
	private WordProcessor simpleWordProcessor;
	
	@Autowired
	private ToneCalculator toneCalculator;
	
	@Autowired
	private AutomaticServiceErrorUtils aseu;
	
	@PostConstruct
	private void readFile() {
		readFileFromPathAndFillIndexes(filePath);
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	// http://seekingalpha.com/symbol/AAPL/transcripts/1
	@Override
	protected String buildUrl(String index, int pageIndex) {
		StringBuilder builder = new StringBuilder(urlBase);
		builder.append(index);
		builder.append(urlMiddle);
		builder.append(pageIndex);
		return builder.toString();
	}

	@Override
	protected boolean notLastPage(String siteContent) {
		return siteContent.contains("\">Next Page</a>");
	}

	@Override
	protected List<EarningsCall> processDocument(String index, Document document) {
		
		List<EarningsCall> ret = Lists.newArrayList();
		
		Element container = document.getElementById("portfolo_selections");
		Elements articles = container.getElementsByTag("a");
		
		for (Element earningsCallArticle : articles) {
			if (elementIsLegalTranscript(earningsCallArticle)) {
				try {
					EarningsCall call = createEarningsCall(earningsCallArticle, index);
					
					if (call.words.size() > 200) {
						// it is probably a real earnings call, not only a link to some audio shit
						processTone(call);
						retrieveRelevantQAndAPartAndProcessTone(call);
					}
					
					ret.add(call);
					Thread.sleep(2000);
				} catch (Exception e) {
					LOGGER.error("Failed to process: (" + earningsCallArticle.text() + ")", e);
					aseu.saveError(AutomaticService.SEEKING_ALPHA, e.getMessage());
				}
			}
		}
		
		return ret;
	}

	private void processTone(EarningsCall call) {
		call.tone = toneCalculator.getToneOf(call.words);
		call.hTone = toneCalculator.getHToneOf(call.words);
	}

	private boolean elementIsLegalTranscript(Element earningsCallArticle) {
		boolean isQP = earningsCallArticle.hasAttr("sasource") && validSASource(earningsCallArticle.attr("sasource"));
		String linkText = earningsCallArticle.text();
		boolean isTranscript = linkText.toLowerCase().contains(EARNINGS_CALL_TRANSCRIPT);
		return isQP && isTranscript;
	}
	
	private boolean validSASource(String src) {
		return StringUtils.equals(src, "qp_analysis") || StringUtils.equals(src, "qp_transcripts");
	}
	
	private EarningsCall createEarningsCall(Element dataRow, String index)
			throws Exception {
		
		String href = dataRow.attr("href");
		String articleUrl = articleUrlBase + href;
		LOGGER.info("Processing: " + articleUrl);
		
		String articleHTMLContent = URLUtils.getHTMLContentOfURL(articleUrl);
		Document doc = Jsoup.parse(articleHTMLContent);
		String articleBody = URLUtils.getContentOfHTMLContent(doc.getElementById("a-body").text());
		
		EarningsCall data = new EarningsCall();
		data.url = articleUrl;
		data.tradingSymbol = index;
		data.rawText = articleBody;
		for (Element dateTime : doc.getElementsByTag("time")) {
			if (StringUtils.equals(dateTime.attr("itemprop"), "dateModified")) {
				data.publishDate = parseDate(dateTime);
				break;
			}
		}
		data.words = simpleWordProcessor.parseArticlePlainTextAndBuildMapOfWords(data.rawText);
		
		return data;
	}

	public void retrieveRelevantQAndAPartAndProcessTone(EarningsCall earningsCall) {
		String[] qAndAParts = earningsCall.rawText.split(QUESTION_AND_ANSWER);
		if (qAndAParts.length >= 2) {
			processQAndA(earningsCall, qAndAParts);
			return;
		}
		
		qAndAParts = earningsCall.rawText.split(QUESTION_AND_ANSWER_2);
		if (qAndAParts.length >= 2) {
			processQAndA(earningsCall, qAndAParts);
			return;
		}
		
	}
	
	public EarningsCall collectLatestForIndex(ProcessFirstNArticleParameter parameterObject) throws Exception {
		return collectLatestNForIndex(parameterObject).get(0);
	}
	
	public List<EarningsCall> collectLatestNForIndex(ProcessFirstNArticleParameter parameterObject) throws Exception {
		LOGGER.info("Collect latest for index: " + parameterObject.index);
		String urlStr = buildUrl(parameterObject.index, 0);
		String siteContent = URLUtils.getHTMLContentOfURL(urlStr);
		Document document = Jsoup.parse(siteContent);
		
		parameterObject.document = document;
		
		return processFirstNArticle(parameterObject);
	}
	
	private List<EarningsCall> processFirstNArticle(ProcessFirstNArticleParameter parameterObject) {
		Element container = parameterObject.document.getElementById("portfolo_selections");
		Elements articles = container.getElementsByTag("a");
		
		int processed = 0;
		List<EarningsCall> processedCalls = new ArrayList<>();
		
		for (Element earningsCallArticle : articles) {
			if (elementIsLegalTranscript(earningsCallArticle)) {
				try {
					EarningsCall call = createEarningsCall(earningsCallArticle, parameterObject.index);
					
					if (call.words.size() > 200) {
						// it is probably a real earnings call, not only a link to some audio shit
						processTone(call);
						retrieveRelevantQAndAPartAndProcessTone(call);
						processedCalls.add(call);
						processed++;
					}
				} catch (Exception e) {
					LOGGER.error("Failed to process: (" + earningsCallArticle.text() + ")", e);
					aseu.saveError(AutomaticService.SEEKING_ALPHA, e.getMessage());
				}
			}
			
			if (processed == parameterObject.count) {
				return processedCalls;
			}
		}
		
		return processedCalls;
	}

	private void processQAndA(EarningsCall earningsCall, String[] qAndAParts) {
		String qAndA = qAndAParts[qAndAParts.length - 1];
		qAndA = qAndA.split(COPYRIGHT_POLICY)[0];
		
		earningsCall.qAndAText = qAndA;
		earningsCall.qAndAWords = simpleWordProcessor.parseArticlePlainTextAndBuildMapOfWords(earningsCall.qAndAText);
		
		if (earningsCall.qAndAWords.size() > 50) {
			// it is probably a real earnings call, not only a link to some audio shit
			earningsCall.qAndATone = toneCalculator.getToneOf(earningsCall.qAndAWords);
			earningsCall.qAndAHTone = toneCalculator.getHToneOf(earningsCall.qAndAWords);
		}
	}
	
	private DateTime parseDate(Element dateTime) {
		try {
			return DateTimeUtils.parseToYYYYMMDD_HHmmss_ZONE_UTC(dateTime.attr("datetime"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			aseu.saveError(AutomaticService.SEEKING_ALPHA, e.getMessage());
			return null;
		}
	}

}
