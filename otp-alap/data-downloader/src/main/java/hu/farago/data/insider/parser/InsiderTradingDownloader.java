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
import org.apache.tika.exception.TikaException;
import org.joda.time.DateTime;
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
	
	private static final SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DecimalFormat simpleNumberFormat;
	private static DecimalFormat dollarNumberFormat;
	private static final int PAGE_SIZE = 20;
	
	{
		NumberFormat nfSimple = NumberFormat.getNumberInstance(Locale.US);
		simpleNumberFormat = (DecimalFormat)nfSimple;
		simpleNumberFormat.applyPattern("###,###,###,###,###,###");
		
		NumberFormat nfDollar = NumberFormat.getNumberInstance(Locale.US);
		dollarNumberFormat = (DecimalFormat)nfDollar;
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
		return (int) (INDEXES.size() / PAGE_SIZE);
	}
	
	public Map<String, List<InsiderData>> parseAll(int pageIdx) throws Exception  {
		
		Map<String, List<InsiderData>> insiderList = Maps.newHashMap();
		
		for (int i = pageIdx * PAGE_SIZE; i < (pageIdx + 1) * PAGE_SIZE; i++) {
			String index = INDEXES.get(i);
			insiderList.put(index, collectAllInsiderDataForIndex(index));
		}
		
		return insiderList;
	}

	private List<InsiderData> collectAllInsiderDataForIndex(String index)
			throws IOException, SAXException, TikaException,
			InsiderTradingException, ParseException {
		List<InsiderData> insiderDataList = Lists.newArrayList();
		
		boolean jumpToNext = true;
		int pageIndex = 0;
		
		while(jumpToNext) {
			String urlStr = buildUrl(index, ++pageIndex);
			String siteContent = URLUtils.getContentForURL(urlStr);
			
			if (siteContent.contains("Next Insider Trading") || 
				siteContent.contains("Next Previous Insider Trading")) {
				jumpToNext = true;
			} else {
				jumpToNext = false;
			}
			
			String[] parts = siteContent.split("Transaction Form ");
			if (parts.length != 2) {
				throw new InsiderTradingException("page was not processable");
			}
			
			String dataPart = parts[1];
			String[] transactions = dataPart.split(" Form \\d ");
			
			for (String transactionRow : transactions) {
				if (!validTransactionRow(transactionRow)) {
					continue;
				}
				
				try {
					insiderDataList.add(createInsiderData(transactionRow, index));
				} catch (Exception e) {
					LOGGER.error("Failed to process: (" + transactionRow + ")", e);
				}
			}
		}
		return insiderDataList;
	}
	
	private boolean validTransactionRow(String transactionRow) {
		if (transactionRow.startsWith("Buy ") || transactionRow.startsWith("Sell ")) {
			return true;
		}
		
		return false;
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
	
	private InsiderData createInsiderData(String dataRow, String index) throws ParseException, InsiderTradingException {
		String[] dataRowParts = dataRow.split(" ");
		
		int idxOfSymbol = searchForSymbolIndexInDataRow(dataRowParts, index);
		int endIndexOfOwnerRelationShip = 0;
		
		InsiderData data = new InsiderData();
		data.type = BuySell.createByName(dataRowParts[0]);
		if (data.type == null) {
			throw new InsiderTradingException("buy/sell type not found");
		}
		
		data.transactionDate = new DateTime(dfDate.parse(dataRowParts[1]));
		data.acceptanceDate = new DateTime(dfTime.parse(dataRowParts[2] + " " + dataRowParts[3]));
		data.issuerTradingSymbol = index;
		
		for (int idx = 4; idx < dataRowParts.length - 1; idx++) {
			OwnerRelationShip ors = OwnerRelationShip.createByName(dataRowParts[idx] + " " + dataRowParts[idx + 1]);
			if (ors != null) {
				data.ownerRelationShip = ors;
				endIndexOfOwnerRelationShip = idx + 1;
				break;
			}
			ors = OwnerRelationShip.createByName(dataRowParts[idx]);
			if (ors != null) {
				data.ownerRelationShip = ors;
				endIndexOfOwnerRelationShip = idx;
				break;
			}
		}
		if (data.ownerRelationShip == null) {
			throw new InsiderTradingException("owner relationship not found");
		}
		
		data.reportingOwnerName = "";
		for (int idx = idxOfSymbol + 1; idx < endIndexOfOwnerRelationShip; idx ++){
			data.reportingOwnerName += dataRowParts[idx] + " ";
		}
		
		int length = dataRowParts.length;
		
		data.transactionShares = tryToParseDouble(simpleNumberFormat, dataRowParts[length - 4]);
		data.pricePerShare = tryToParseDouble(dollarNumberFormat, dataRowParts[length - 3]);
		data.totalValue = tryToParseDouble(dollarNumberFormat, dataRowParts[length - 2]);
		data.sharesOwned = tryToParseDouble(simpleNumberFormat, dataRowParts[length - 1]);
		
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

	private int searchForSymbolIndexInDataRow(String[] dataRowParts,
			String index) throws InsiderTradingException {
		
		for (int idx = 4; idx < dataRowParts.length; idx++) {
			if (dataRowParts[idx].equalsIgnoreCase(index)) {
				return idx;
			}
		}
		
		throw new InsiderTradingException("index not found");
	}
	
	
}
