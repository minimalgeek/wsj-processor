package hu.farago.data.edgar;

import hu.farago.data.api.DataDownloader;
import hu.farago.data.edgar.dto.EdgarXML;
import hu.farago.data.model.entity.mongo.EdgarData;
import hu.farago.data.utils.URLUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

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
import com.google.common.collect.Sets;

@Component
public class EdgarDownloader extends DataDownloader<EdgarData> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EdgarDownloader.class);
	
	@Autowired
	private XMLToEdgarConverter converter;

	// we use the same ticker list!
	@Value("${insider.filePath}")
	private String filePath;

	@Value("${edgar.urlBase}")
	private String edgarUrlBase;
	
	@Value("${edgar.urlMiddle}")
	private String edgarUrlMiddle;
	
	@Value("${edgar.urlEnd}")
	private String edgarUrlEnd;
	
	@Value("${edgar.urlRoot}")
	private String edgarUrlRoot;
	
	private Set<String> urls;

	@PostConstruct
	private void readFile() {
		readFileFromPathAndFillIndexes(filePath);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected String buildUrl(String index, int pageIndex) {
		StringBuilder builder = new StringBuilder(edgarUrlBase);
		builder.append(index);
		builder.append(edgarUrlMiddle);
		builder.append((pageIndex-1)*40);
		builder.append(edgarUrlEnd);
		builder.append(40);
		return builder.toString();
	}

	@Override
	protected boolean notLastPage(String siteContent) {
		return StringUtils.contains(siteContent, "value=\"Next 40\"");
	}

	@Override
	protected List<EdgarData> processDocument(String index, Document document) {
		
		List<EdgarData> list = Lists.newArrayList();
		
		Element table = document.getElementsByClass("tableFile2").first();
		Elements transactions = table.getElementsByTag("tbody").get(0)
				.getElementsByTag("tr");

		for (Element transactionRow : transactions) {
			try {
				Elements tds = transactionRow.getElementsByTag("td");
				if (tds.size() > 0) {
					String formURL = edgarUrlRoot + tds.get(1).child(0).attr("href");
					if (!urls.contains(formURL)) {
						EdgarData createdData = createEdgarData(formURL, index);
						if (createdData != null) {
							list.add(createdData);
							urls.add(formURL);
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("Failed to process: (" + transactionRow.text()
						+ ")", e);
			}
		}

		return list;
	}

	private EdgarData createEdgarData(String url, String index) throws IOException, SAXException, TikaException, JAXBException {
		String siteContent = URLUtils.getHTMLContentOfURL(url);
		Document document = Jsoup.parse(siteContent);

		Elements table = document.getElementsByTag("a");
		
		for (Element element : table) {
			if (element.text().endsWith(".xml")) {
				String xmlUrl = edgarUrlRoot + element.attr("href");
				String xmlContent = URLUtils.getHTMLContentOfURL(xmlUrl);
				
				EdgarXML edgarXML = converter.convertXMLToEdgar(xmlContent);
				LOGGER.info("Edgar XML processed: " + edgarXML.issuer.issuerTradingSymbol + " => " + edgarXML.reportingOwner.reportingOwnerId.rptOwnerName);
				
				EdgarData data = new EdgarData();
				data.edgarXML = edgarXML;
				data.tradingSymbol = index;
				data.formURL = xmlUrl;
				
				return data;
			}
		}
		
		return null;
	}
	
	

	@Override
	public void clean() {
		urls = Sets.newHashSet();
	}

}
