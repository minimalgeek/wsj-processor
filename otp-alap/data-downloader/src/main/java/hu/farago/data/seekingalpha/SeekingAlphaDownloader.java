package hu.farago.data.seekingalpha;

import hu.farago.data.api.DataDownloader;
import hu.farago.data.api.WordProcessor;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.utils.URLUtils;

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

@Component
public class SeekingAlphaDownloader extends DataDownloader<EarningsCall> {

	public static final String QUESTION_AND_ANSWER = "(?i)question-and-answer";
	public static final String QUESTION_AND_ANSWER_2 = "(?i)question and answer";
	public static final String COPYRIGHT_POLICY = "(?i)copyright policy";

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
	protected void processDocument(String index, Document document,
			List<EarningsCall> dataList) {
		Element container = document.getElementById("portfolo_selections");
		Elements articles = container.getElementsByTag("a");
		
		for (Element earningsCallArticle : articles) {
			if (earningsCallArticle.hasAttr("sasource") && earningsCallArticle.attr("sasource").equals("qp_transcripts")) {
				try {
					EarningsCall call = createEarningsCall(earningsCallArticle, index);
					
					if (call.words.size() > 200) {
						// it is probably a real earnings call, not only a link to some audio shit
						call.tone = toneCalculator.getToneOf(call.words);
						call.hTone = toneCalculator.getHToneOf(call.words);
					}
					
					dataList.add(call);
					Thread.sleep(2000);
				} catch (Exception e) {
					LOGGER.error("Failed to process: (" + earningsCallArticle.text() + ")", e);
				}
			}
		}
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
		data.tradingSymbol = index;
		data.rawText = articleBody;
		for (Element dateTime : doc.getElementsByTag("time")) {
			if (StringUtils.equals(dateTime.attr("itemprop"), "datePublished")) {
				data.publishDate = DateTime.parse(dateTime.attr("content"));
				break;
			}
		}
		data.words = simpleWordProcessor.parseArticlePlainTextAndBuildMapOfWords(data.rawText);
		
		return data;
	}

	public void retrieveRelevantQAndAPartAndProcessTone(EarningsCall earningsCall) {
		String[] qAndAParts = earningsCall.rawText.split(QUESTION_AND_ANSWER);
		if (qAndAParts.length == 2) {
			processQAndA(earningsCall, qAndAParts);
			return;
		}
		
		qAndAParts = earningsCall.rawText.split(QUESTION_AND_ANSWER_2);
		if (qAndAParts.length == 2) {
			processQAndA(earningsCall, qAndAParts);
			return;
		}
		
	}

	private void processQAndA(EarningsCall earningsCall, String[] qAndAParts) {
		String qAndA = qAndAParts[1];
		qAndA = qAndA.split(COPYRIGHT_POLICY)[0];
		
		earningsCall.qAndAText = qAndA;
		earningsCall.qAndAWords = simpleWordProcessor.parseArticlePlainTextAndBuildMapOfWords(earningsCall.qAndAText);
		
		if (earningsCall.qAndAWords.size() > 50) {
			// it is probably a real earnings call, not only a link to some audio shit
			earningsCall.qAndATone = toneCalculator.getToneOf(earningsCall.qAndAWords);
			earningsCall.qAndAHTone = toneCalculator.getHToneOf(earningsCall.qAndAWords);
		}
	}
}
