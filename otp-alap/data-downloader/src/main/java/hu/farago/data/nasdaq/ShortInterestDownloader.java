package hu.farago.data.nasdaq;

import hu.farago.data.model.entity.mongo.ShortInterest;
import hu.farago.data.utils.URLUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class ShortInterestDownloader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShortInterestDownloader.class);

	private static final DateTimeFormatter formatter = DateTimeFormat
			.forPattern("MM/dd/yyyy");
	private static final DecimalFormat decimalFormatter = new DecimalFormat(
			"###,###.######", new DecimalFormatSymbols(Locale.US));

	@Value("${nasdaq.shortInterest.urlBase}")
	private String nasdaqshortInterestUrlBase;

	@Value("${nasdaq.shortInterest.urlEnd}")
	private String nasdaqshortInterestUrlEnd;

	public List<ShortInterest> downloadShortInterestsForTradingSymbol(
			String tradingSymbol) throws Exception {
		String url = nasdaqshortInterestUrlBase + tradingSymbol
				+ nasdaqshortInterestUrlEnd;
		LOGGER.info("Downloading short interest from: " + url);

		String siteContent = URLUtils.getHTMLContentOfURL(url);
		Document document = Jsoup.parse(siteContent);
		List<ShortInterest> ret = Lists.newArrayList();

		Element container = document
				.getElementById("quotes_content_left_ShortInterest1_ShortInterestGrid");

		if (container != null) {
			Elements interests = container.getElementsByTag("tr");

			for (Element interest : interests) {
				Elements tds = interest.getElementsByTag("td");

				if (tds.size() == 4) {
					ShortInterest shortInterest = createShortInterestFromTds(tds);
					shortInterest.tradingSymbol = tradingSymbol;
					ret.add(shortInterest);
				}
			}
		}
		return ret;
	}

	private ShortInterest createShortInterestFromTds(Elements tds) {
		ShortInterest interest = new ShortInterest();

		interest.settlementDate = formatter.parseDateTime(tds.get(0).text())
				.withZoneRetainFields(DateTimeZone.UTC);
		interest.shortInterest = parseElementToDouble(tds.get(1));
		interest.avgDailyShareVolume = parseElementToDouble(tds.get(2));
		interest.daysToCover = parseElementToDouble(tds.get(3));

		return interest;
	}

	private double parseElementToDouble(Element tds) {
		try {
			return decimalFormatter.parse(tds.text()).doubleValue();
		} catch (ParseException ex) {
			return 0.0;
		}
	}
}
