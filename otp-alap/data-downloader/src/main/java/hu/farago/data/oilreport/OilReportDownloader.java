package hu.farago.data.oilreport;

import hu.farago.data.api.UrlBasedDownloader;
import hu.farago.data.api.WordProcessor;
import hu.farago.data.model.dao.mongo.OilReportRepository;
import hu.farago.data.model.entity.mongo.OilReport;
import hu.farago.data.seekingalpha.ToneCalculator;
import hu.farago.data.seekingalpha.dto.HTone;
import hu.farago.data.utils.DateTimeUtils;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class OilReportDownloader extends UrlBasedDownloader<OilReport> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OilReportDownloader.class);

	@Value("${oilreport.urlBase}")
	private String oilreportUrlBase;

	@Autowired
	private ToneCalculator calculator;

	@Autowired
	private WordProcessor simpleWordProcessor;

	@Autowired
	private OilReportRepository repository;

	private StringBuilder articleTextBuilder;

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected List<String> buildUrls() throws Exception {
		List<String> urls = Lists.newArrayList();
		int nowYear = DateTime.now().getYear();
		int nowMonth = DateTime.now().getMonthOfYear();
		for (int year = 2007; year <= nowYear; year++) {
			for (int i = 1; i <= 12; i++) {
				if (year == nowYear && i > nowMonth) {
					break;
				}
				String month = StringUtils.leftPad("" + i, 2, '0');
				String url = oilreportUrlBase + year + "/" + month + StringUtils.substring("" + year, 2);
				urls.add(url);
			}
		}

		return urls;
	}

	@Override
	protected OilReport buildDocument(Document document) {
		articleTextBuilder = new StringBuilder();

		OilReport report = new OilReport();
		String dateText = document.getElementsByTag("h1").first().ownText();
		dateText = StringUtils.substringAfter(dateText, "Oil Market Report: ");
		report.publicationDate = DateTimeUtils.parseToDDMMMMYYYY_UTC(dateText);

		report.highlightsTone = buildTone("Highlights", document);
		report.overviewTone = buildTone("Overview", document);
		report.demandTone = buildTone("Demand", document);
		report.supplyTone = buildTone("Supply", document);
		report.oecdStocksTone = buildTone("OECD Stocks", document);
		report.pricesTone = buildTone("Prices", document);
		report.refiningTone = buildTone("Refining", document);

		// try {
		// FileUtils.writeStringToFile(FileUtils.getFile("C:/DEV/temp/files/",
		// year + "-" + month + ".txt"), articleTextBuilder.toString(),
		// "UTF-8");
		// } catch (IOException e) {
		// LOGGER.error(e.getMessage());
		// }

		report.sumTotalTone = new HTone();
		report.sumTotalTone.positiveCount = report.highlightsTone.positiveCount
				+ report.overviewTone.positiveCount
				+ report.demandTone.positiveCount
				+ report.supplyTone.positiveCount
				+ report.oecdStocksTone.positiveCount
				+ report.pricesTone.positiveCount
				+ report.refiningTone.positiveCount;

		report.sumTotalTone.negativeCount = report.highlightsTone.negativeCount
				+ report.overviewTone.negativeCount
				+ report.demandTone.negativeCount
				+ report.supplyTone.negativeCount
				+ report.oecdStocksTone.negativeCount
				+ report.pricesTone.negativeCount
				+ report.refiningTone.negativeCount;
		return report;
	}

	@Override
	protected MongoRepository<OilReport, BigInteger> getRepository() {
		return repository;
	}


	private HTone buildTone(String articlePartHeader, Document document) {
		Element articlePart = document.getElementById(articlePartHeader);

		if (articlePart != null) {
			Elements sumElements = articlePart.getElementsByTag("p");
			sumElements.addAll(articlePart.getElementsByTag("li"));

			for (Element element : sumElements) {
				String text = element.text();
				articleTextBuilder.append(text);
			}
			return calculator.getHToneOf(simpleWordProcessor
					.parseArticlePlainTextAndBuildMapOfWords(articleTextBuilder
							.toString()));
		}
		return new HTone();
	}

}
