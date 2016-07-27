package hu.farago.data.sandp;

import hu.farago.data.model.entity.mongo.SAndPIndex;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.Event;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;
import hu.farago.data.utils.URLUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class SAndPFileWriter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SAndPFileWriter.class);

	private static final DateFormat YMD = new SimpleDateFormat("yyyy.MM.dd");

	@Value("${spice.indices.dirRoot}")
	private String directoryRoot;

	@Value("${spice.indices.tlsFile}")
	private String tlsFileName;

	private Map<SAndPGroup, List<CSVData>> fileDatas;
	private List<String> tlsData;
	private List<String> tlsAlreadyAdded;

	public void reloadFileDatas() throws IOException, ParseException {
		fileDatas = Maps.newHashMap();
		for (SAndPGroup group : SAndPGroup.values()) {
			File indexGroupFile = new File(FilenameUtils.concat(directoryRoot,
					group.getName() + ".csv"));
			List<String> content = FileUtils.readLines(indexGroupFile,
					URLUtils.UTF_8);

			List<CSVData> datas = Lists.newArrayList();
			fileDatas.put(group, datas);

			for (String line : content) {
				datas.add(new CSVData(line));
			}
		}

		File tlsFile = new File(
				FilenameUtils.concat(directoryRoot, tlsFileName));
		tlsData = FileUtils.readLines(tlsFile, URLUtils.UTF_8);
		tlsAlreadyAdded = Lists.newArrayList();
	}

	public void writeFileDatas() throws IOException {
		for (SAndPGroup group : SAndPGroup.values()) {
			File indexGroupFile = new File(FilenameUtils.concat(directoryRoot,
					group.getName() + ".csv"));
			fileDatas.get(group).sort(
					(c1, c2) -> (c1.ticker.compareTo(c2.ticker)));
			List<String> stringDataList = Lists.newArrayList();
			for (CSVData data : fileDatas.get(group)) {
				stringDataList.add(data.toString());
			}

			FileUtils.writeLines(indexGroupFile, stringDataList, false);
		}
		
		tlsData.sort((c1, c2) -> (c1.compareTo(c2)));
		File tlsFile = new File(
				FilenameUtils.concat(directoryRoot, tlsFileName));
		FileUtils.writeLines(tlsFile, tlsData, false);
	}

	public void addSAndPIndex(SAndPIndex index) throws Exception {
		LOGGER.info("writeSAndPIndicesToFile");

		for (SAndPOperation operation : index.operations) {
			boolean found = false;
			List<CSVData> list = fileDatas.get(operation.indexGroup);
			for (CSVData data : list) {
				if (StringUtils.equals(data.ticker, index.tradingSymbol)) {
					if (data.operationIsTheSame(operation)) {
						found = true;
						break;
					}
				}
			}

			if (!found) {
				if (operation.event.equals(Event.ADD)) {
					list.add(new CSVData(index.tradingSymbol,
							operation.eventDate.toDate(), null));
				} else if (operation.event.equals(Event.DROP)) {
					Optional<CSVData> dataToModify = list
							.stream()
							.filter(csv -> csv.ticker
									.equals(index.tradingSymbol)
									&& csv.outDate == null
									&& (csv.inDate == null || csv.inDate
											.before(operation.eventDate
													.toDate()))).findFirst();
					if (dataToModify.isPresent()) {
						dataToModify.get().outDate = operation.eventDate
								.toDate();
					} else {
						list.add(new CSVData(index.tradingSymbol,
								null,
								operation.eventDate.toDate()));
					}
				}
			}
			
			handleTLS(index.tradingSymbol, operation);
		}
	}

	private void handleTLS(String tradingSymbol, SAndPOperation operation) {
		if (operation.indexGroup.equals(SAndPGroup.SP400) || 
			operation.indexGroup.equals(SAndPGroup.SP500) || 
			operation.indexGroup.equals(SAndPGroup.SP600)) {
			if (operation.event.equals(Event.ADD) && !tlsData.contains(tradingSymbol)) {
				tlsData.add(tradingSymbol);
				tlsAlreadyAdded.add(tradingSymbol);
			} else if (operation.event.equals(Event.DROP) && tlsData.contains(tradingSymbol) && !tlsAlreadyAdded.contains(tradingSymbol)) {
				tlsData.remove(tradingSymbol);
			}
		}
		
	}

	private class CSVData {

		public String ticker;
		public Date inDate;
		public Date outDate;

		public CSVData(String data) throws ParseException {
			String[] parts = StringUtils
					.splitByWholeSeparatorPreserveAllTokens(data, ",");

			ticker = parts[0];
			if (StringUtils.isNotEmpty(parts[1])) {
				inDate = YMD.parse(parts[1]);
			}
			if (StringUtils.isNotEmpty(parts[2])) {
				outDate = YMD.parse(parts[2]);
			}
		}

		public CSVData(String ticker, Date inDate, Date outDate) {
			super();
			this.ticker = ticker;
			this.inDate = inDate;
			this.outDate = outDate;
		}

		public boolean operationIsTheSame(SAndPOperation op) {
			return op.event.equals(Event.ADD) && inDate != null
					&& DateUtils.isSameDay(inDate, op.eventDate.toDate())
					|| op.event.equals(Event.DROP) && outDate != null
					&& DateUtils.isSameDay(outDate, op.eventDate.toDate());
		}

		@Override
		public String toString() {
			return ticker + "," + (inDate == null ? "" : YMD.format(inDate))
					+ "," + (outDate == null ? "" : YMD.format(outDate));
		}
	}
}
