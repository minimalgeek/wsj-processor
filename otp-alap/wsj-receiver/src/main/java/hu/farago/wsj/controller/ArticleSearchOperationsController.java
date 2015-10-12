package hu.farago.wsj.controller;

import hu.farago.wsj.controller.dto.CompanyInfoDTO;
import hu.farago.wsj.model.dao.mongo.ArticleAggregateManager;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;
import hu.farago.wsj.nasdaq.CompanyInfoReaderWriter;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@RestController
public class ArticleSearchOperationsController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ArticleSearchOperationsController.class);

	@Autowired
	private ArticleAggregateManager aggregateManager;
	@Autowired
	private CompanyInfoReaderWriter companyInfoRW;

	@RequestMapping(value = "/exportDatesToCSV", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<String> exportDatesToCSV() throws IOException {
		LOGGER.info("export found dates of NASDAQ 100 tickers");

		List<String> indexesAndNumberOfDates = Lists.newArrayList();

		for (CompanyInfoDTO companyInfo : companyInfoRW.readAllTickers()) {
			LOGGER.info("searching for: " + companyInfo.toString());
			List<ArticleCollection> articles = aggregateManager.findAllByFullTextSearch(companyInfo);
			
			List<DateTime> dates = Lists.transform(articles, new Function<ArticleCollection, DateTime>() {

				@Override
				public DateTime apply(ArticleCollection input) {
					return new DateTime(input.getDateTime());
				}
				
			});
			
			indexesAndNumberOfDates.add(companyInfo.getIndex() + ": " + articles.size());
			companyInfoRW.writeRelevantDatesToFile(companyInfo, dates);
		}

		return indexesAndNumberOfDates;
	}

}
