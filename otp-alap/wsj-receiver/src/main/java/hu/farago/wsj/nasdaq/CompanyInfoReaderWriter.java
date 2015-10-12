package hu.farago.wsj.nasdaq;

import hu.farago.wsj.controller.dto.CompanyInfoDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class CompanyInfoReaderWriter {

	private static final String NAS100_FILE_PATH = "nas100_tickers.csv";
	
	@Value("${wsj.receiver.csvPath}")
	private String csvPath;
	
	public List<CompanyInfoDTO> readAllTickers() throws IOException {
		InputStream stream = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(NAS100_FILE_PATH);
		List<String> lines = IOUtils.readLines(stream);
		
		List<CompanyInfoDTO> retList = Lists.newArrayList();
		
		for (String line : lines) {
			String[] parts = StringUtils.split(line, ';');
			CompanyInfoDTO dto = new CompanyInfoDTO(parts[0], parts[1]);
			retList.add(dto);
		}
		
		return retList;
		
	}

	public File writeRelevantDatesToFile(CompanyInfoDTO dto,
			List<DateTime> dates) throws IOException {
		File fileToWrite = new File(csvPath + dto.getIndex() + ".csv");
		if (!fileToWrite.exists()) {
			fileToWrite.createNewFile();
		}
		
		List<String> lines = Lists.newArrayList();
		for (DateTime dateTime : dates) {
			lines.add(dateTime.toString("MM/dd/yyyy"));
		}
		
		FileUtils.writeLines(fileToWrite, "UTF-8", lines, true);
		
		return fileToWrite;
	}
	
}
