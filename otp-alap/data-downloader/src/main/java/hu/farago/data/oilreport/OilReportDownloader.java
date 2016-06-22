package hu.farago.data.oilreport;

import hu.farago.data.api.WordProcessor;
import hu.farago.data.model.entity.mongo.OilReport;
import hu.farago.data.seekingalpha.ToneCalculator;
import hu.farago.data.seekingalpha.dto.HTone;
import hu.farago.data.utils.DateTimeUtils;
import hu.farago.data.utils.URLUtils;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

@Component
public class OilReportDownloader {

	private static final Logger LOGGER = LoggerFactory.getLogger(OilReportDownloader.class);
	
	@Value("${oilreport.urlBase}")
	private String oilreportUrlBase;
	
	@Autowired
	private ToneCalculator calculator;
	
	@Autowired
	private WordProcessor simpleWordProcessor;
	
	private StringBuilder articleTextBuilder;
	
	public List<OilReport> downloadAllForYear(int year) throws IOException, SAXException, TikaException {
		
		List<OilReport> ret = Lists.newArrayList();
		
		for (int i = 1; i <= 12; i++) {
			String month = StringUtils.leftPad("" + i, 2, '0');
			String url = oilreportUrlBase + year + "/" + month + StringUtils.substring("" + year, 2);
			LOGGER.info("Download content from: " + url);
			
			String siteContent = null;
			try {
				siteContent = URLUtils.getHTMLContentOfURL(url);
			} catch (Exception e) {
				LOGGER.info("URL is not valid, skip this: " + url);
			}
			
			if (siteContent != null) {
				Document document = Jsoup.parse(siteContent);
				
				OilReport report = buildReportFromDocument(year, month, url,
						document);
				ret.add(report);
			}
		}
		
		return ret;
	}

	private OilReport buildReportFromDocument(int year, String month,
			String url, Document document) {

		articleTextBuilder = new StringBuilder();
		
		OilReport report = new OilReport();
		report.url = url;
		report.publicationDate = DateTimeUtils.parseToYYYYMM_UTC(year + "-" + month);
		
		report.highlightsTone = buildTone("Highlights", document);
		report.overviewTone = buildTone("Overview", document);
		report.demandTone = buildTone("Demand", document);
		report.supplyTone = buildTone("Supply", document);
		report.oecdStocksTone = buildTone("OECD Stocks", document);
		report.pricesTone = buildTone("Prices", document);
		report.refiningTone = buildTone("Refining", document);

//		try {
//			FileUtils.writeStringToFile(FileUtils.getFile("C:/DEV/temp/files/", year + "-" + month + ".txt"), articleTextBuilder.toString(), "UTF-8");
//		} catch (IOException e) {
//			LOGGER.error(e.getMessage());
//		}
		
		report.sumTotalTone = new HTone();
		report.sumTotalTone.positiveCount = 
				report.highlightsTone.positiveCount +
				report.overviewTone.positiveCount + 
				report.demandTone.positiveCount + 
				report.supplyTone.positiveCount + 
				report.oecdStocksTone.positiveCount + 
				report.pricesTone.positiveCount + 
				report.refiningTone.positiveCount;
		
		report.sumTotalTone.negativeCount = 
				report.highlightsTone.negativeCount +
				report.overviewTone.negativeCount + 
				report.demandTone.negativeCount + 
				report.supplyTone.negativeCount + 
				report.oecdStocksTone.negativeCount + 
				report.pricesTone.negativeCount + 
				report.refiningTone.negativeCount;
		return report;
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
			return calculator.getHToneOf(simpleWordProcessor.parseArticlePlainTextAndBuildMapOfWords(articleTextBuilder.toString()));
		}
		return new HTone();
	}
	
}
