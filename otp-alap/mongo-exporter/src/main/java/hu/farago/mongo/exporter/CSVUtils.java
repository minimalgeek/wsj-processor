package hu.farago.mongo.exporter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Lists;

public class CSVUtils<T> {
	
	private String workingPath;
	
	public CSVUtils(String workingPath) {
		this.workingPath = workingPath;
	}

	public void writeInsiderDataToCSVFiles(Map<String, List<T>> data)
			throws IOException {
		for (Map.Entry<String, List<T>> company : data.entrySet()) {
			processData(company);
		}
	}

	private void processData(Map.Entry<String, List<T>> company) throws IOException {

		ParameterDTO dto = new ParameterDTO();
		dto.setCsvFile(createFileIfNotExistsForSymbol(workingPath, company.getKey()));

		for (T dtoData : company.getValue()) {
			if (CollectionUtils.isEmpty(dto.getFileContent())) {
				dto.getFileContent().add(processDataAsCSVHeader(dtoData));
			}
			dto.getFileContent().add(processDataAsCSVLine(dtoData));
		}
		
		dto.writeLinesToFile();
	}

	private File createFileIfNotExistsForSymbol(String path, String symbol)
			throws IOException {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		String fileName = StringUtils.join(path, symbol, ".csv");
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
	
	private String processDataAsCSVLine(T data) {
		
		StringBuilder builder = new StringBuilder();
		
		for (Field field : FieldUtils.getAllFieldsList(data.getClass())) {
			builder.append(ReflectionUtils.getField(field, data).toString());
			builder.append(",");
		}
		
		return builder.substring(0, builder.length() - 1);

	}
	
	private String processDataAsCSVHeader(T data) {
		
		StringBuilder builder = new StringBuilder();
		
		for (Field field : FieldUtils.getAllFieldsList(data.getClass())) {
			builder.append(field.getName());
			builder.append(",");
		}
		
		return builder.substring(0, builder.length() - 1);
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
			this.fileContent = FileUtils.readLines(csvFile);
		}

		public void writeLinesToFile() throws IOException {
			FileUtils.writeLines(csvFile, "UTF-8", fileContent, false);
		}

		public List<String> getFileContent() {
			return fileContent;
		}

	}
	
}
