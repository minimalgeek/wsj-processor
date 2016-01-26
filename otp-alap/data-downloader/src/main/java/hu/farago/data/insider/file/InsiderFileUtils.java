package hu.farago.data.insider.file;

import hu.farago.data.insider.dto.InsiderData;
import hu.farago.data.insider.dto.InsiderData.BuySell;
import hu.farago.data.utils.URLUtils;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class InsiderFileUtils {

	@Value("${insider.pathBuy}")
	private String pathToCSVBuys;
	@Value("${insider.pathSell}")
	private String pathToCSVSells;
	
	@Value("${insider.dateFormat}")
	private String dateFormat;

	public void writeInsiderDataToCSVFiles(Map<String, List<InsiderData>> data)
			throws IOException {
		for (Map.Entry<String, List<InsiderData>> company : data.entrySet()) {
			processDataByType(BuySell.BUY, company);
			processDataByType(BuySell.SELL, company);
		}
	}

	private void processDataByType(BuySell type,
			Map.Entry<String, List<InsiderData>> company) throws IOException {

		ParameterDTO dto = new ParameterDTO();
		String actualWorkingPath = (type == BuySell.BUY ? pathToCSVBuys
				: pathToCSVSells);
		dto.setCsvFile(createFileIfNotExistsForSymbol(actualWorkingPath,
				company.getKey()));

		CSVDataDTO dataDTO = null;
		List<CSVDataDTO> dataDTOList = Lists.newArrayList();

		for (InsiderData insiderData : company.getValue()) {
			if (insiderData.type == type) {
				if (dataDTO == null) {
					dataDTO = new CSVDataDTO(insiderData);
					continue;
				}
				DateTime tempDate = insiderData.acceptanceDate
						.withTimeAtStartOfDay();
				if (dataDTO.dateOfAcceptance.isEqual(tempDate)) {
					dataDTO.transactionSum += insiderData.totalValue;
				} else {
					dataDTOList.add(dataDTO);
					dataDTO = new CSVDataDTO(insiderData);
				}
			}
		}
		
		dataDTOList.sort(new Comparator<CSVDataDTO>() {
			@Override
			public int compare(CSVDataDTO o1, CSVDataDTO o2) {
				return o1.dateOfAcceptance.compareTo(o2.dateOfAcceptance);
			}
		});

		for (CSVDataDTO csvDataDTO : dataDTOList) {
			dto.getFileContent().add(csvDataDTO.toString());
		}

		dto.writeLinesToFile();
	}

	private File createFileIfNotExistsForSymbol(String path, String symbol)
			throws IOException {
		String fileName = StringUtils.join(path, symbol, ".csv");
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * This data should be written to files
	 * 
	 * @author Balázs
	 */
	private class CSVDataDTO {
		public DateTime dateOfAcceptance;
		public double transactionSum;

		public CSVDataDTO(InsiderData data) {
			this.dateOfAcceptance = data.acceptanceDate.withTimeAtStartOfDay();
			this.transactionSum = data.totalValue;
		}

		public String toString() {
			return dateOfAcceptance.toString(dateFormat) + ","
					+ transactionSum;
		}
	}

	/**
	 * Holds data for the file writing process, and helps to create the 'helper
	 * data' as well.
	 * 
	 * @author Balázs
	 */
	private class ParameterDTO {

		private File csvFile;
		private List<String> fileContent = Lists.newArrayList();

		public void setCsvFile(File csvFile) throws IOException {
			this.csvFile = csvFile;
		}

		public void writeLinesToFile() throws IOException {
			FileUtils.writeLines(csvFile, URLUtils.UTF_8, fileContent, false);
		}

		public List<String> getFileContent() {
			return fileContent;
		}

	}
}
