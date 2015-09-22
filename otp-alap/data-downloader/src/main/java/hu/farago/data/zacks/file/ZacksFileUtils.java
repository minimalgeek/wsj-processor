package hu.farago.data.zacks.file;


import hu.farago.data.zacks.service.DataSynchronizerService;
import hu.farago.data.zacks.service.dto.CompanyData;
import hu.farago.data.zacks.service.dto.ZacksData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
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

			dto.setCsvFileAndFillFileContent(createFileIfNotExistsForSymbol(company
					.getSymbol()));
			dto.setNextReportDateStrWithYYYY(createReportDateWith20YearPrefix(company
					.getNextReportDate()));

			if (!CollectionUtils.isEmpty(dto.getFileContent())) {
				updateOrAppendNextReportDateInCSV(dto);
			} else {
				appendNextReportDateInCSV(dto);
			}

		}
	}

	private void updateOrAppendNextReportDateInCSV(
			ZacksFileUtilsParameterDTO dto) throws IOException {
		dto.setLastReportDateStrAndCreateLastReportDate(Iterables.getLast(dto
				.getFileContent()));

		if (dto.lastReportDateIsFutureButNotSameWithNextReportDate()) {
			Iterables.removeIf(dto.getFileContent(), new Predicate<String>() {
				@Override
				public boolean apply(String line) {
					return line.equals(dto.getLastReportDateStr());
				}
			});
			appendNextReportDateInCSV(dto);
		} else if (dto.getLastReportDate().before(new Date())) {
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
		String nextReportDateWithYYYY;
		if (dateParts.length == 3) {
			nextReportDateWithYYYY = dateParts[0] + '/' + dateParts[1] + "/20"
					+ dateParts[2];
		} else {
			nextReportDateWithYYYY = nextReportDate;
		}
		return nextReportDateWithYYYY;
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
		dto.getFileContent().add(dto.getNextReportDateStrWithYYYY());
		FileUtils.writeLines(dto.getCsvFile(), DataSynchronizerService.UTF_8, dto.getFileContent(),
				false);
	}

	/**
	 * Holds data for the file writing process, and helps to create the 'helper data' as well.
	 * @author Balázs
	 *
	 */
	private class ZacksFileUtilsParameterDTO {

		private File csvFile;
		private String lastReportDateStr;
		private Date lastReportDate;
		private String nextReportDateStrWithYYYY;
		private List<String> fileContent;

		public boolean lastReportDateIsFutureButNotSameWithNextReportDate() {
			return lastReportDate.after(new Date())
					&& !lastReportDateStr.equals(nextReportDateStrWithYYYY);
		}

		public File getCsvFile() {
			return this.csvFile;
		}

		public void setCsvFileAndFillFileContent(File csvFile)
				throws IOException {
			this.csvFile = csvFile;
			this.fileContent = FileUtils.readLines(this.csvFile,
					DataSynchronizerService.UTF_8);
		}

		public String getLastReportDateStr() {
			return lastReportDateStr;
		}

		public void setLastReportDateStrAndCreateLastReportDate(
				String lastReportDateStr) {
			this.lastReportDateStr = lastReportDateStr;

			try {
				this.lastReportDate = DateUtils.parseDate(lastReportDateStr,
						"MM/dd/yyyy");
			} catch (ParseException ex) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(1970, 1, 1);
				this.lastReportDate = calendar.getTime();

			}
		}

		public Date getLastReportDate() {
			return lastReportDate;
		}
		
		public String getNextReportDateStrWithYYYY() {
			return nextReportDateStrWithYYYY;
		}

		public void setNextReportDateStrWithYYYY(
				String nextReportDateStrWithYYYY) {
			this.nextReportDateStrWithYYYY = nextReportDateStrWithYYYY;
		}

		public List<String> getFileContent() {
			return fileContent;
		}

	}
}
