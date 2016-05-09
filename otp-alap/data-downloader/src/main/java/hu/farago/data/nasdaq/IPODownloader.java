package hu.farago.data.nasdaq;

import hu.farago.data.model.entity.mongo.IPOActivity;
import hu.farago.data.utils.DateTimeUtils;
import hu.farago.data.utils.URLUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
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
public class IPODownloader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IPODownloader.class);
	private static final DecimalFormat dollarFormat = new DecimalFormat(
			"$###,###,###,###.##", DecimalFormatSymbols.getInstance(Locale.US));
	private static final DecimalFormat sharesFormat = new DecimalFormat(
			"###,###,###,###", DecimalFormatSymbols.getInstance(Locale.US));

	@Value("${nasdaq.ipo.urlBase}")
	private String nasdaqIpoUrlBase;
	@Value("${nasdaq.ipo.dateFrom}")
	private String nasdaqIpoDateFrom;

	/**
	 * Downloads all activity for given month
	 * 
	 * @param yearAndMonth
	 *            string date with format: yyyy-mm
	 * @return
	 * @throws Exception
	 */
	public List<IPOActivity> downloadAllActivityInMonth(String yearAndMonth)
			throws Exception {

		String url = nasdaqIpoUrlBase + yearAndMonth;

		String content = URLUtils.getHTMLContentOfURL(url);
		Document document = Jsoup.parse(content);

		List<IPOActivity> ret = Lists.newArrayList();

		Element container = document.getElementsByClass("genTable").first();

		if (container != null) {
			Elements activities = container.getElementsByTag("tbody").first()
					.getElementsByTag("tr");

			for (Element activityTD : activities) {
				Elements tds = activityTD.getElementsByTag("td");

				if (tds.size() == 7) {
					createAndAddActivityToList(ret, tds);
				}
			}
		}
		return ret;

	}
	
	public List<IPOActivity> downloadAllActivity() {
		DateTime from = DateTimeUtils.parseToYYYYMM_UTC(nasdaqIpoDateFrom);
		DateTime to = DateTime.now();
		
		List<IPOActivity> ret = Lists.newArrayList();
		for (DateTime date = from; date.isBefore(to); date = date.plusMonths(1))
		{
			String dateToDownload = DateTimeUtils.formatToYYYYMM(date);
			try {
				List<IPOActivity> listForMonth = downloadAllActivityInMonth(dateToDownload);
				ret.addAll(listForMonth);
			} catch (Exception e) {
				LOGGER.error("Failed month: " + dateToDownload, e);
			}
		}
		
		return ret;
	}
	
	public List<IPOActivity> downloadCurrentMonthActivity() throws Exception {
		return downloadAllActivityInMonth(DateTimeUtils.formatToYYYYMM(DateTime.now()));
	}

	private void createAndAddActivityToList(List<IPOActivity> ret, Elements tds) {
		try {
			IPOActivity activity = new IPOActivity();
			activity.name = tds.get(0).text();
			activity.tradingSymbol = tds.get(1).text();
			activity.market = tds.get(2).text();
			activity.price = dollarFormat.parse(tds.get(3).text())
					.doubleValue();
			activity.shares = sharesFormat.parse(tds.get(4).text()).doubleValue();
			activity.offerAmount = dollarFormat.parse(tds.get(5).text())
					.doubleValue();
			activity.datePriced = DateTimeUtils.parseToMMDDYYYY_UTC(tds.get(6).text());
			
			ret.add(activity);
			LOGGER.info("Activity added: " + activity.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
