package hu.farago.data.service;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import hu.farago.data.model.dao.mongo.ZacksEarningsCallDates2Repository;
import hu.farago.data.model.entity.mongo.AutomaticServiceError.AutomaticService;
import hu.farago.data.model.entity.mongo.ZacksEarningsCallDates2;
import hu.farago.data.utils.AutomaticServiceErrorUtils;
import hu.farago.data.utils.URLUtils;
import hu.farago.data.zacks.ZacksFileUtils;
import hu.farago.data.zacks.ZacksStockQuoteDownloader;
import hu.farago.data.zacks.dto.ZacksData;

@RestController
public class ZacksDownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZacksDownloadService.class);

	@Value("#{'${zacks.urls}'.split(',')}")
	private List<String> zacksURLList;

	@Autowired
	private ZacksFileUtils zacksFileUtils;

	@Autowired
	private ZacksStockQuoteDownloader downloader;

	@Autowired
	private ZacksEarningsCallDates2Repository repository;

	@Autowired
	private AutomaticServiceErrorUtils aseu;

	@RequestMapping(value = "/refreshAllReportDates", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<String> refreshAllReportDates() {
		LOGGER.info("refreshAllReportDates");

		List<String> refreshedURLs = Lists.newArrayList();

		for (String url : zacksURLList) {
			try {
				String content = URLUtils.getContentOfURL(url);
				ZacksData zacksData = createZacksDataFromContent(content);
				zacksFileUtils.writeZacksDataToCSVFiles(zacksData);

				refreshedURLs.add(url);
			} catch (Exception ex) {
				LOGGER.error("Exception happened during URL content open or processing", ex);
				aseu.saveError(AutomaticService.ZACKS, ex.getMessage());
			}
		}

		return refreshedURLs;
	}

	@RequestMapping(value = "/downloadAllZECD", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<String> downloadAllZECD() {
		LOGGER.info("downloadAllZECD");

		try {
			List<ZacksEarningsCallDates2> list = downloader.downloadAllZECD();
			List<String> retList = Lists.newArrayList();

			for (ZacksEarningsCallDates2 zecd2 : list) {

				if (nextReportDateIsInTheFuture(zecd2)) {
					List<ZacksEarningsCallDates2> oldRecords = repository.findByTradingSymbol(zecd2.tradingSymbol);
					for (ZacksEarningsCallDates2 record : oldRecords) {
						if (nextReportDateIsInTheFuture(record)) {
							repository.delete(record);
						}
					}
					repository.save(zecd2);
					retList.add(zecd2.toString());
				}
			}

			return retList;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			aseu.saveError(AutomaticService.ZACKS, e.getMessage());
		}

		return null;
	}

	@Scheduled(cron = "0 0 6 * * ?")
	private void downloadAllZECDScheduled() {
		try {
			downloadAllZECD();
		} catch (Exception e) {
			aseu.saveError(AutomaticService.ZACKS, e.getMessage());
		}
	}

	private boolean nextReportDateIsInTheFuture(ZacksEarningsCallDates2 record) {
		return record.nextReportDate.isAfterNow()
				|| record.nextReportDate.withTimeAtStartOfDay().isEqual(DateTime.now().withTimeAtStartOfDay());
	}

	public ZacksData createZacksDataFromContent(String massContent)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(massContent, ZacksData.class);
	}

}
