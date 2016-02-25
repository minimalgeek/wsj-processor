package hu.farago.data.insider;

import hu.farago.data.api.DataDownloader;
import hu.farago.data.model.entity.mongo.InsiderData;
import hu.farago.data.model.entity.mongo.InsiderData.BuySell;
import hu.farago.data.model.entity.mongo.InsiderData.OwnerRelationShip;

import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InsiderDownloader extends DataDownloader<InsiderData> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InsiderDownloader.class);

	@Value("${insider.filePath}")
	private String filePath;
	@Value("${insider.urlBase}")
	private String urlBase;
	
	@Value("${insider.fromDate}")
	private String fromDateString;
	@Value("${insider.toDate}")
	private String toDateString;

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
	protected void processDocument(String index, Document document, List<InsiderData> dataList) {
		Element table = document.getElementById("tracker");
		Elements transactions = table.getElementsByTag("tbody").get(0).getElementsByTag("tr");
		
		for (Element transactionRow : transactions) {
			try {
				dataList.add(createInsiderData(transactionRow, index));
			} catch (Exception e) {
				LOGGER.error("Failed to process: (" + transactionRow.text() + ")",
						e);
			}
		}
	}

	private InsiderData createInsiderData(Element dataRow, String index)
			throws ParseException {
		
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
	
}
