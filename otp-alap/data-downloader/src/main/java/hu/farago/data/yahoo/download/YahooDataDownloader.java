package hu.farago.data.yahoo.download;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class YahooDataDownloader {

	@Value("#{'${currency.pairs}'.split(',')}")
	private List<String> currencyPairs;
	
}
