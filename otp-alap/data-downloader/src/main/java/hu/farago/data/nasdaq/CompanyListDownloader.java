package hu.farago.data.nasdaq;

import hu.farago.data.utils.URLUtils;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

@Component
public class CompanyListDownloader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CompanyListDownloader.class);
	
	@Value("${nasdaq.companies.urlBase}")
	private String nasdaqCompaniesUrlBase;
	
	@Value("${nasdaq.companies.urlEnd}")
	private String nasdaqCompaniesUrlEnd;
	
	public List<String> downloadExchangeCompanyList(Exchange exchange) throws IOException, SAXException, TikaException {
		String url = nasdaqCompaniesUrlBase + exchange.getName() + nasdaqCompaniesUrlEnd;
		LOGGER.info("Downloading companies from: " + url);
		
		String content = URLUtils.getHTMLContentOfURL(url);
		String lines[] = content.split("\\r?\\n");
		
		List<String> ret = Lists.newArrayList();
		for (String line : lines) {
			String symbol =StringUtils.substringBetween(line, "\"", "\""); 
			if (symbol.length() <= 4 && symbol.length() >= 2) {
				ret.add(symbol);
			}
		}
		return ret;
	}
	
}
