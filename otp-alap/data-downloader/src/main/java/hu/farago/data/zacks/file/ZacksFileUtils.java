package hu.farago.data.zacks.file;

import hu.farago.data.zacks.service.DataSynchronizerService;
import hu.farago.data.zacks.service.dto.CompanyData;
import hu.farago.data.zacks.service.dto.ZacksData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Component
public class ZacksFileUtils {

	@Value("${zacks.path}")
	private String pathToCSVs;

	public void writeZacksDataToCSVFiles(ZacksData data) throws IOException {
		for (CompanyData company : data.getData()) {

			ZacksFileUtilsParameterDTO dto = new ZacksFileUtilsParameterDTO();
			dto.setCsvFile(createFileIfNotExistsForSymbol(company.getSymbol()));
			
			String reportDate = createReportDateWith20YearPrefix(company
					.getNextReportDate());
			if (reportDate == null) {
				continue;
			}
			dto.setNextReportDateStr(reportDate);

			if (CollectionUtils.isEmpty(dto.getFileContent())) {
				appendNextReportDateInCSV(dto);
			} else {
				updateOrAppendNextReportDateInCSV(dto);
			}

		}
	}

	private void updateOrAppendNextReportDateInCSV(
			ZacksFileUtilsParameterDTO dto) throws IOException {
		dto.setLastReportDateStr(Iterables.getLast(dto.getFileContent()));

		if (dto.lastReportDateIsAfterToday() && dto.lastAndNextAreNotOnTheSameDay()) {
			Iterables.removeIf(dto.getFileContent(), new Predicate<String>() {
				@Override
				public boolean apply(String line) {
					return line.equals(dto.getLastReportDateStr());
				}
			});
			appendNextReportDateInCSV(dto);
		} else if (dto.lastReportDateIsBeforeToday()) {
			// append the next report date to the list
			appendNextReportDateInCSV(dto);
		}
	}

	/**
	 * Converts '11/16/15' to '11/16/2015'
	 * 
	 * @param nextReportDate
	 * @return
	 */
	private String createReportDateWith20YearPrefix(String nextReportDate) {
		String[] dateParts = StringUtils.split(nextReportDate, '/');
		if (dateParts.length == 3) {
			return dateParts[0] + '/' + dateParts[1] + "/20" + dateParts[2];
		}

		return null;
	}

	private File createFileIfNotExistsForSymbol(String symbol)
			throws IOException {
		String fileName = StringUtils.join(pathToCSVs, symbol, ".csv");
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	private void appendNextReportDateInCSV(ZacksFileUtilsParameterDTO dto)
			throws IOException {
		dto.getFileContent().add(dto.getNextReportDateStr());
		FileUtils.writeLines(dto.getCsvFile(), DataSynchronizerService.UTF_8,
				dto.getFileContent(), false);
	}

	/**
	 * Holds data for the file writing process, and helps to create the 'helper
	 * data' as well.
	 * 
	 * @author Balázs
	 *
	 */
	private class ZacksFileUtilsParameterDTO {

		private File csvFile;
		private List<String> fileContent;

		private String lastReportDateStr;
		private String nextReportDateStr;

		private DateTime lastReportDate;
		private DateTime nextReportDate;
		private DateTime today;
		
		private DateTimeFormatter formatter;

		public ZacksFileUtilsParameterDTO() {
			today = DateTime.now(DateTimeZone.UTC).withTimeAtStartOfDay();
			formatter = new DateTimeFormatterFactory("MM/dd/yyyy").createDateTimeFormatter().withZone(DateTimeZone.UTC);
		}

		public boolean lastReportDateIsAfterToday() {
			return lastReportDate.isAfter(today);
		}

		public boolean lastReportDateIsBeforeToday() {
			return lastReportDate.isBefore(today);
		}
		
		public boolean lastAndNextAreNotOnTheSameDay() {
			return !lastReportDate.equals(nextReportDate);
		}

		public File getCsvFile() {
			return csvFile;
		}

		public void setCsvFile(File csvFile) throws IOException {
			this.csvFile = csvFile;
			this.fileContent = FileUtils.readLines(csvFile, DataSynchronizerService.UTF_8);
			Iterables.removeIf(this.fileContent, new Predicate<String>() {
				@Override
				public boolean apply(String input) {
					return StringUtils.isEmpty(input);
				}
			});
		}

		public List<String> getFileContent() {
			return fileContent;
		}

		public String getLastReportDateStr() {
			return lastReportDateStr;
		}

		public void setLastReportDateStr(String lastReportDateStr) {
			this.lastReportDateStr = lastReportDateStr;
			this.lastReportDate = DateTime.parse(this.lastReportDateStr, this.formatter);
		}

		public String getNextReportDateStr() {
			return nextReportDateStr;
		}

		public void setNextReportDateStr(String nextReportDateStr) {
			this.nextReportDateStr = nextReportDateStr;
			this.nextReportDate = DateTime.parse(this.nextReportDateStr, this.formatter);
		}

	}
}
