package hu.farago.data.zacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.farago.data.model.dao.mongo.EarningsCallRepository;
import hu.farago.data.model.dao.mongo.ZacksEarningsCallDatesRepository;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.model.entity.mongo.ZacksEarningsCallDates;
import hu.farago.data.seekingalpha.SeekingAlphaDownloader;

public class ScheduledSeekingAlphaCheck implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ScheduledSeekingAlphaCheck.class);
	
	private SeekingAlphaDownloader downloader;
	private ZacksEarningsCallDates jobScheduledBy;
	private ZacksEarningsCallDatesRepository zacksRepository;
	private EarningsCallRepository ecRepository;

	public ScheduledSeekingAlphaCheck(SeekingAlphaDownloader downloader,
			ZacksEarningsCallDates jobScheduledBy,
			ZacksEarningsCallDatesRepository zacksRepository,
			EarningsCallRepository ecRepository) {
		super();
		this.downloader = downloader;
		this.jobScheduledBy = jobScheduledBy;
		this.zacksRepository = zacksRepository;
		this.ecRepository = ecRepository;
	}

	@Override
	public void run() {
		LOGGER.info("Scheduled Job on: " + jobScheduledBy.toString());
		
		jobScheduledBy = zacksRepository.findByTradingSymbolAndNextReportDate(
				jobScheduledBy.tradingSymbol, 
				jobScheduledBy.nextReportDate);
		
		if (jobScheduledBy != null && jobScheduledBy.foundEarningsCallId == null) {
			searchForEarningsCall();
		}
	}

	private void searchForEarningsCall() {
		try {
			EarningsCall call = downloader.collectLatestForIndex(jobScheduledBy.tradingSymbol);
			if (call != null && call.publishDate.isAfter(jobScheduledBy.nextReportDate.minusDays(1))) {
				call = ecRepository.save(call);
				
				jobScheduledBy.foundEarningsCallId = call.id;
				zacksRepository.save(jobScheduledBy);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
