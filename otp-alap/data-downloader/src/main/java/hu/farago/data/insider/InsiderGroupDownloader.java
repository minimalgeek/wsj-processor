package hu.farago.data.insider;

import hu.farago.data.api.DataDownloader;
import hu.farago.data.model.entity.mongo.InsiderDataGroup;
import hu.farago.data.model.entity.mongo.InsiderDataGroup.OwnerRelationShip;
import hu.farago.data.model.entity.mongo.embedded.FormData;
import hu.farago.data.utils.URLUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Component
public class InsiderGroupDownloader extends DataDownloader<InsiderDataGroup> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InsiderGroupDownloader.class);

	@Value("${insider.filePath}")
	private String filePath;
	@Value("${insider.urlBase}")
	private String urlBase;

	@Value("${insider.urlRoot}")
	private String urlRoot;

	@Value("${insider.fromDate}")
	private String fromDateString;
	@Value("${insider.toDate}")
	private String toDateString;

	private Set<String> urls;
	
	@PostConstruct
	private void readFile() {
		readFileFromPathAndFillIndexes(filePath);
	}

	@Override
	protected String buildUrl(String index, int page) {
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

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected boolean notLastPage(String siteContent) {
		return siteContent.contains(">Next</a>");
	}

	@Override
	protected List<InsiderDataGroup> processDocument(String index, Document document) {

		List<InsiderDataGroup> insiderList = Lists.newArrayList();
		
		Element table = document.getElementById("tracker");
		Elements transactions = table.getElementsByTag("tbody").get(0)
				.getElementsByTag("tr");

		for (Element transactionRow : transactions) {
			try {
				Elements tds = transactionRow.getElementsByTag("td");
				String formURL = tds.get(11).child(0).attr("href");
				if (!urls.contains(formURL)) {
					insiderList.add(createInsiderData(tds, index));
					urls.add(formURL);
				}
			} catch (Exception e) {
				LOGGER.error("Failed to process: (" + transactionRow.text()
						+ ")", e);
			}
		}

		return insiderList;
	}
	
	@Override
	public void clean() {
		urls = Sets.newHashSet();
	}

	private InsiderDataGroup createInsiderData(Elements tds, String index)
			throws Exception {

		InsiderDataGroup data = new InsiderDataGroup();

		data.transactionDate = parseDate(tds, 1);
		data.acceptanceDate = parseDate(tds, 2);
		data.issuerName = tds.get(3).text();
		data.issuerTradingSymbol = index;
		data.reportingOwnerName = tds.get(5).text();
		data.ownerRelationShip = OwnerRelationShip.createByName(StringUtils
				.strip(tds.get(6).text()));
		data.formURL = tds.get(11).child(0).attr("href");

		createFormData(data);
		// data.transactionShares = tryToParseDouble(simpleNumberFormat,
		// tds.get(7).text());

		return data;
	}

	private void createFormData(InsiderDataGroup data) throws Exception {
		String url = urlRoot + data.formURL;

		String siteContent = URLUtils.getHTMLContentOfURL(url);
		Document document = Jsoup.parse(siteContent);

		Elements form4 = document.getElementsByClass("form4");
		Element derivativeTable = form4.get(1);

		data.formDataList = processTable(derivativeTable);
	}

	private List<FormData> processTable(Element table) throws ParseException {
		List<FormData> retList = Lists.newArrayList();

		Elements transactions = table.getElementsByTag("tbody").get(0)
				.getElementsByTag("tr");
		for (int i = 2; i < transactions.size(); i++) {
			Element transaction = transactions.get(i);
			Elements tds = transaction.getElementsByTag("td");
			if (tds.size() == 11) {
				FormData data = new FormData();

				data.titleOfSecurity = stripToFirst(tds, 0);
				data.transactionDate = parseDate(tds, 1);
				// we skip 2nd idx
				String[] codes = strip(tds, 3).split(" ");
				if (codes.length == 3) {
					data.code01 = NumberUtils.toInt(codes[0]);
					data.code02 = codes[1];
					data.code03 = NumberUtils.toInt(codes[2]);
				}
				data.code04 = strip(tds, 4);

				data.amountOfAcquired = NumberUtils.toDouble(stripToFirst(tds,
						5));
				data.acquired = strip(tds, 6);
				data.priceOfAcquired = NumberUtils
						.toDouble(stripToFirst(tds, 7));

				data.sharesOwned = NumberUtils.toDouble(stripToFirst(tds, 8));
				data.ownershipForm = strip(tds, 9);

				retList.add(data);
			}
		}
		
		return retList;
	}

	private String strip(Elements tds, int idx) {
		return StringUtils.strip(tds.get(idx).text());
	}

	private String stripToFirst(Elements tds, int idx) {
		return strip(tds, idx).split(" <sup>")[0];
	}

	private DateTime parseDate(Elements tds, int idx) {
		try {
			return new DateTime(dfDate.parse(tds.get(idx).text())).withZoneRetainFields(DateTimeZone.UTC);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

}
