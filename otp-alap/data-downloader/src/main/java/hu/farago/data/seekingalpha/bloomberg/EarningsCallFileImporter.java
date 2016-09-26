package hu.farago.data.seekingalpha.bloomberg;

import hu.farago.data.api.WordProcessor;
import hu.farago.data.model.dao.mongo.EarningsCallRepository;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.seekingalpha.ToneCalculator;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class EarningsCallFileImporter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EarningsCallFileImporter.class);
	private static final DateTimeFormatter DATETIME = DateTimeFormat
			.forPattern("yyyyMMdd");

	public static final String QUESTION_AND_ANSWER = "Q&A [\\r\\n]+";
	public static final String COPYRIGHT_POLICY = "© COPYRIGHT ";

	@Value("${seekingalpha.importDirectory}")
	private String importDirectory;
	@Autowired
	private EarningsCallRepository repository;
	@Autowired
	private WordProcessor simpleWordProcessor;
	@Autowired
	private ToneCalculator toneCalculator;

	public void importAll() {
		List<EarningsCall> ret = Lists.newArrayList();

		Collection<File> files = FileUtils.listFiles(new File(importDirectory),
				new SuffixFileFilter(".txt"), TrueFileFilter.TRUE);
		
		for (File file : files) {
			try {
				EarningsCall ec = createEarningsCall(file);
				repository.delete(repository.findByUrl(ec.url));
				repository.save(ec);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		repository.save(ret);
	}

	private EarningsCall createEarningsCall(File file) throws Exception {

		String articleURL = file.getAbsolutePath();
		LOGGER.info("Processing: " + articleURL);
		String articleBody = FileUtils.readFileToString(file, "UTF-8");

		EarningsCall data = new EarningsCall();
		data.url = articleURL;
		data.tradingSymbol = parseIndex(file);
		data.rawText = articleBody;
		data.publishDate = parseDate(file);
		data.words = simpleWordProcessor
				.parseArticlePlainTextAndBuildMapOfWords(data.rawText);
		processTone(data);
		retrieveRelevantQAndAPartAndProcessTone(data);

		return data;
	}

	private void processTone(EarningsCall call) {
		call.tone = toneCalculator.getToneOf(call.words);
		call.hTone = toneCalculator.getHToneOf(call.words);
	}

	private void retrieveRelevantQAndAPartAndProcessTone(
			EarningsCall earningsCall) {
		String[] qAndAParts = earningsCall.rawText.split(QUESTION_AND_ANSWER);
		if (qAndAParts.length >= 2) {
			processQAndA(earningsCall, qAndAParts);
			return;
		}
	}

	private void processQAndA(EarningsCall earningsCall, String[] qAndAParts) {
		String qAndA = qAndAParts[qAndAParts.length - 1];
		qAndA = qAndA.split(COPYRIGHT_POLICY)[0];

		earningsCall.qAndAText = qAndA;
		earningsCall.qAndAWords = simpleWordProcessor
				.parseArticlePlainTextAndBuildMapOfWords(earningsCall.qAndAText);

		if (earningsCall.qAndAWords.size() > 50) {
			// it is probably a real earnings call, not only a link to some
			// audio shit
			earningsCall.qAndATone = toneCalculator
					.getToneOf(earningsCall.qAndAWords);
			earningsCall.qAndAHTone = toneCalculator
					.getHToneOf(earningsCall.qAndAWords);
		}
	}

	/**
	 * Filename looks like: AMLN_2002Q4_20030220_1600.txt
	 * 
	 * @param file
	 * @return
	 */
	private DateTime parseDate(File file) {
		String[] dateTimeTextParts = StringUtils.split(file.getName(), "_");
		return DATETIME.parseDateTime(dateTimeTextParts[2]);
	}

	private String parseIndex(File file) {
		return StringUtils.split(file.getName(), "_")[0];
	}
}
