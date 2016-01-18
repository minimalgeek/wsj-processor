package hu.farago.data.insider.parser;

import hu.farago.data.insider.dto.InsiderData;
import hu.farago.data.insider.dto.InsiderData.BuySell;
import hu.farago.data.insider.dto.InsiderData.OwnerRelationShip;
import hu.farago.data.utils.URLUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class InsiderTradingDownloader {

	public static class InsiderTradingException extends Exception {
		private static final long serialVersionUID = 834931039021500042L;
		private static final String MSG_BASE = "Site content is not processable";

		public InsiderTradingException() {
			super(MSG_BASE);
		}

		public InsiderTradingException(String msg) {
			super(MSG_BASE + ", because " + msg);
		}
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InsiderTradingDownloader.class);

	private static final SimpleDateFormat dfDate = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat dfTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static DecimalFormat simpleNumberFormat;
	private static DecimalFormat dollarNumberFormat;
	private static final int PAGE_SIZE = 20;

	{
		NumberFormat nfSimple = NumberFormat.getNumberInstance(Locale.US);
		simpleNumberFormat = (DecimalFormat) nfSimple;
		simpleNumberFormat.applyPattern("###,###,###,###,###,###");

		NumberFormat nfDollar = NumberFormat.getNumberInstance(Locale.US);
		dollarNumberFormat = (DecimalFormat) nfDollar;
		dollarNumberFormat.applyPattern("$###,###,###,###,###,##0.####");
	}

	public static List<String> INDEXES;

	@Value("${insider.filePath}")
	private String filePath;
	@Value("${insider.urlBase}")
	private String urlBase;
	@Value("#{new java.text.SimpleDateFormat('${insider.dateFormat}').parse('${insider.fromDate}')}")
	private Date fromDate;
	@Value("${insider.fromDate}")
	private String fromDateString;
	@Value("#{new java.text.SimpleDateFormat('${insider.dateFormat}').parse('${insider.toDate}')}")
	private Date toDate;
	@Value("${insider.toDate}")
	private String toDateString;

	@PostConstruct
	private void readFile() {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(filePath);
		File sAndPFile = new File(url.getFile());

		try {
			INDEXES = FileUtils.readLines(sAndPFile, Charset.forName("UTF-8"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public static int pages() {
		if (INDEXES.size() % PAGE_SIZE == 0) {
			return (int) (INDEXES.size() / PAGE_SIZE);
		} else {
			return (int) (INDEXES.size() / PAGE_SIZE) + 1;
		}
	}

	public Map<String, List<InsiderData>> parseAll(int pageIdx)
			throws Exception {

		Map<String, List<InsiderData>> insiderList = Maps.newHashMap();

		for (int i = pageIdx * PAGE_SIZE; i < (pageIdx + 1) * PAGE_SIZE && i < INDEXES.size(); i++) {
			String index = INDEXES.get(i);
			try {
				insiderList.put(index, collectAllInsiderDataForIndex(index));
				LOGGER.info(index + " processed");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		return insiderList;
	}

	private List<InsiderData> collectAllInsiderDataForIndex(String index)
			throws IOException, SAXException, TikaException,
			InsiderTradingException, ParseException {
		List<InsiderData> insiderDataList = Lists.newArrayList();

		boolean jumpToNext = true;
		int pageIndex = 0;

		while (jumpToNext) {
			String urlStr = buildUrl(index, ++pageIndex);
			String siteContent = URLUtils.getHTMLContentForURL(urlStr);
			Document document = Jsoup.parse(siteContent);

			if (siteContent.contains(">Next</a>")) {
				jumpToNext = true;
			} else {
				jumpToNext = false;
			}
			
			Element table = document.getElementById("tracker");
			Elements transactions = table.getElementsByTag("tbody").get(0).getElementsByTag("tr");
			
			for (Element transactionRow : transactions) {
				try {
					insiderDataList
							.add(createInsiderData(transactionRow, index));
				} catch (Exception e) {
					LOGGER.error("Failed to process: (" + transactionRow.text() + ")",
							e);
				}
			}
		}
		return insiderDataList;
	}

	private String buildUrl(String index, int page) {
		StringBuilder builder = new StringBuilder(urlBase);
		builder.append(index);
		builder.append("&date_from=");
		builder.append(fromDateString);
		builder.append("&date_to=");
		builder.append(toDateString);
		builder.append("&submit=+GO+&page=");
		builder.append(page);
		return builder.toString();
	}

	private InsiderData createInsiderData(Element dataRow, String index)
			throws ParseException, InsiderTradingException {
		
		Elements tds = dataRow.getElementsByTag("td");
		InsiderData data = new InsiderData();

		data.type = BuySell.createByName(StringUtils.strip(tds.get(0).text()));
		data.transactionDate = new DateTime(dfDate.parse(tds.get(1).text()));
		data.acceptanceDate = new DateTime(dfTime.parse(tds.get(2).text()));
		data.issuerName = tds.get(3).text();
		data.issuerTradingSymbol = index;
		data.reportingOwnerName = tds.get(5).text();
		data.ownerRelationShip = OwnerRelationShip.createByName(StringUtils.strip(tds.get(6).text()));
		data.transactionShares = tryToParseDouble(simpleNumberFormat, tds.get(7).text());
		data.pricePerShare = tryToParseDouble(dollarNumberFormat, tds.get(8).text());
		data.totalValue = tryToParseDouble(dollarNumberFormat, tds.get(9).text());
		data.sharesOwned = tryToParseDouble(simpleNumberFormat, tds.get(10).text());
		
		return data;
	}

	private double tryToParseDouble(DecimalFormat format, String text) {
		try {
			return format.parse(text).doubleValue();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return 0.0;
		}
	}

}
