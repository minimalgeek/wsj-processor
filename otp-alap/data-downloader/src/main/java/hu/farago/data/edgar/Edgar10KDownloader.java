package hu.farago.data.edgar;

import hu.farago.data.api.DataDownloader;
import hu.farago.data.model.entity.mongo.Edgar10QData;
import hu.farago.data.utils.URLUtils;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
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
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

@Component
public class Edgar10KDownloader extends DataDownloader<Edgar10QData> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Edgar10KDownloader.class);

	// we use the same ticker list!
	@Value("${insider.filePath}")
	private String filePath;

	@Value("${edgar.10k.urlBase}")
	private String edgar10kUrlBase;

	@Value("${edgar.10k.urlMiddle}")
	private String edgar10kUrlMiddle;

	@Value("${edgar.10k.urlEnd}")
	private String edgar10kUrlEnd;

	@Value("${edgar.urlRoot}")
	private String edgarUrlRoot;
	
	private FootnoteSearcher searcher;

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
	
	@PostConstruct
	private void postConstruct() {
		readFileFromPathAndFillIndexes(filePath);
		searcher = new DNRFootnoteSearcher();
	}

	@Override
	protected String buildUrl(String index, int pageIndex) {
		StringBuilder builder = new StringBuilder(edgar10kUrlBase);
		builder.append(index);
		builder.append(edgar10kUrlMiddle);
		builder.append((pageIndex - 1) * 40);
		builder.append(edgar10kUrlEnd);
		builder.append(40);
		return builder.toString();
	}

	@Override
	protected boolean notLastPage(String siteContent) {
		return StringUtils.contains(siteContent, "value=\"Next 40\"");
	}

	@Override
	protected List<Edgar10QData> processDocument(String index, Document document) {
		List<Edgar10QData> list = Lists.newArrayList();

		Element table = document.getElementsByClass("tableFile2").first();
		Elements transactions = table.getElementsByTag("tbody").get(0)
				.getElementsByTag("tr");

		for (Element transactionRow : transactions) {
			try {
				Elements tds = transactionRow.getElementsByTag("td");
				if (tds.size() > 0) {
					String formURL = edgarUrlRoot
							+ tds.get(1).child(0).attr("href");
					Edgar10QData createdData = createEdgarData(formURL, index);
					if (createdData != null) {
						list.add(createdData);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Failed to process: (" + transactionRow.text()
						+ ")", e);
			}
		}

		return list;
	}

	private Edgar10QData createEdgarData(String url, String index)
			throws IOException, SAXException, TikaException, JAXBException {
		String siteContent = URLUtils.getHTMLContentOfURL(url);
		Document document = Jsoup.parse(siteContent);

		Elements table = document.getElementsByTag("a");

		for (Element element : table) {
			if (element.text().endsWith("10q.htm")) {
				String htmlURL = edgarUrlRoot + element.attr("href");
				LOGGER.info("=====# PROCESSING: " + htmlURL + " #=====");
				Document formDocument = Jsoup.connect(htmlURL).get();
				StringBuilder builder = searcher.searchFootNotes(formDocument, null);

				long fullLength = formDocument.text().length();
				long footnoteLength = builder.length();

				LOGGER.info("Full length: " + fullLength);
				LOGGER.info("Footnote length: " + footnoteLength);

				Element reportDate = document.getElementsByClass("formGrouping").first().getElementsByClass("info").first();
				Edgar10QData ret = new Edgar10QData();
				ret.footnoteLength = footnoteLength;
				ret.formLength = fullLength;
				ret.formURL = htmlURL;
				ret.tradingSymbol = index;
				ret.reportDate = FORMATTER.parseDateTime(reportDate.text()).withZoneRetainFields(DateTimeZone.UTC);

				return ret;
			}
		}

		return null;
	}

	private interface FootnoteSearcher {
		
		StringBuilder searchFootNotes(Document formDocument, String tag);
		
	}
	
	private class DNRFootnoteSearcher implements FootnoteSearcher {

		@Override
		public StringBuilder searchFootNotes(Document formDocument, String tag) {
			Elements noteHeaders = formDocument
					.getElementsMatchingOwnText("Notes to Unaudited Condensed Consolidated Financial Statements");
			StringBuilder builder = new StringBuilder();
			
			for (Element footnoteSup : noteHeaders) {
				try {
					if (footnoteSup.tagName().equals("font") && 
						footnoteSup.text().length() == 62) {
						
						Element mainParentDiv = footnoteSup.parent().parent();
						Element next = mainParentDiv.nextElementSibling();
						while(next != null) {
							if (next.tagName().equals("hr")) {
								break;
							}
							String siblingContent = next.text();
							builder.append(siblingContent);
							LOGGER.info(siblingContent);
							
							next = next.nextElementSibling();
						}
					}
				} catch (Exception e) {
					// LOGGER.error(e.getMessage(), e);
				}
			}
			return builder;
		}
	}
}
