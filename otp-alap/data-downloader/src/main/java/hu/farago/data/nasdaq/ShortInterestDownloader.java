package hu.farago.data.nasdaq;

import hu.farago.data.model.entity.mongo.ShortInterest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class ShortInterestDownloader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShortInterestDownloader.class);
	
	@Value("${nasdaq.shortInterest.urlBase}")
	private String nasdaqshortInterestUrlBase;
	
	@Value("${nasdaq.shortInterest.urlEnd}")
	private String nasdaqshortInterestUrlEnd;
	
	public List<ShortInterest> downloadShortInterestsForTradingSymbol(String tradingSymbol) {
		List<ShortInterest> ret = Lists.newArrayList();
		
		return ret;
	}
}
