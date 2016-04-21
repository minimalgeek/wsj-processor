package hu.farago.data.service;

import hu.farago.data.model.dao.mongo.ShortInterestRepository;
import hu.farago.data.model.entity.mongo.ShortInterest;
import hu.farago.data.nasdaq.CompanyListDownloader;
import hu.farago.data.nasdaq.Exchange;
import hu.farago.data.nasdaq.ShortInterestDownloader;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.google.common.collect.Sets;

@RestController
public class ShortInterestDownloadService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShortInterestDownloadService.class);
	
	@Autowired
	private CompanyListDownloader companyDownloader;
	
	@Autowired
	private ShortInterestDownloader shortInterestDownloader;
	
	@Autowired
	private ShortInterestRepository repository;
	
	@RequestMapping(value = "/downloadShortInterestData", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Object[] downloadShortInterestData() {
		LOGGER.info("downloadShortInterestData");
		Set<String> companies = Sets.newConcurrentHashSet();
		
		try {
			addCompanyListToCompanies(companies, Exchange.NASDAQ);
			addCompanyListToCompanies(companies, Exchange.NYSE);
			addCompanyListToCompanies(companies, Exchange.AMEX);
			
			for (String tradingSymbol : companies) {
				List<ShortInterest> interests = repository.findByTradingSymbol(tradingSymbol);
				List<ShortInterest> newInterests = shortInterestDownloader.downloadShortInterestsForTradingSymbol(tradingSymbol);
				
				newInterests.forEach(item->{
					if(!interests.contains(item)){
						repository.save(item);
					}
				});
			}
		
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return companies.toArray();
	}

	private boolean addCompanyListToCompanies(Set<String> companies, Exchange ex)
			throws IOException, SAXException, TikaException {
		return companies.addAll(Sets.newConcurrentHashSet(companyDownloader.downloadExchangeCompanyList(ex)));
	}
}
