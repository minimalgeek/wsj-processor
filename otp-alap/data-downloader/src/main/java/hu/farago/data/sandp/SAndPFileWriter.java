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

	private class TLSFile {
		public File file;
		public List<String> tlsData;
		public List<String> tlsAlreadyAdded;

		public TLSFile(String fileName) throws IOException {
			this.file = new File(FilenameUtils.concat(directoryRoot, fileName));
			this.tlsData = FileUtils.readLines(this.file, URLUtils.UTF_8);
			this.tlsAlreadyAdded = Lists.newArrayList();
		}

		public void writeDataToFile() throws IOException {
			tlsData.sort((c1, c2) -> (c1.compareTo(c2)));
			FileUtils.writeLines(file, tlsData, false);
		}
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SAndPFileWriter.class);

	private static final DateFormat YMD = new SimpleDateFormat("yyyy.MM.dd");

	@Value("${spice.indices.dirRoot}")
	private String directoryRoot;

	@Value("${spice.indices.tlsFile}")
	private String tlsFileName;

	@Value("${spice.indices.tlsFileSP45}")
	private String tlsFileNameSP45;

	private Map<SAndPGroup, List<CSVData>> fileDatas;

	private TLSFile us456;
	private TLSFile us45;

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

		us456 = new TLSFile(tlsFileName);
		us45 = new TLSFile(tlsFileNameSP45);
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

		us456.writeDataToFile();
		us45.writeDataToFile();
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
						list.add(new CSVData(index.tradingSymbol, null,
								operation.eventDate.toDate()));
					}
				}
			}

			handleTLS(index.tradingSymbol, operation);
		}
	}

	private void handleTLS(String tradingSymbol, SAndPOperation operation) {

		if (operation.indexGroup.equals(SAndPGroup.SP400)
				|| operation.indexGroup.equals(SAndPGroup.SP500)
				|| operation.indexGroup.equals(SAndPGroup.SP600)) {
			addOrRemoveTLS(tradingSymbol, operation, us456);
		}

		if (operation.indexGroup.equals(SAndPGroup.SP400)
				|| operation.indexGroup.equals(SAndPGroup.SP500)) {
			addOrRemoveTLS(tradingSymbol, operation, us45);
		}
	}

	private void addOrRemoveTLS(String tradingSymbol, SAndPOperation operation,
			TLSFile tls) {
		if (operation.event.equals(Event.ADD)
				&& !tls.tlsData.contains(tradingSymbol)) {
			tls.tlsData.add(tradingSymbol);
			tls.tlsAlreadyAdded.add(tradingSymbol);
		} else if (operation.event.equals(Event.DROP)
				&& tls.tlsData.contains(tradingSymbol)
				&& !tls.tlsAlreadyAdded.contains(tradingSymbol)) {
			tls.tlsData.remove(tradingSymbol);
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
