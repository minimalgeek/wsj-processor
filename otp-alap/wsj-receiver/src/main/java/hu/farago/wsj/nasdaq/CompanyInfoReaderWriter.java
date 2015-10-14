package hu.farago.wsj.nasdaq;

import hu.farago.wsj.controller.dto.CompanyInfoDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
			CompanyInfoDTO dto = new CompanyInfoDTO(parts[0], Lists.newArrayList(ArrayUtils.subarray(parts, 1, parts.length)));
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

		Map<String, Long> datesWithCount = Maps.newLinkedHashMap();
		for (DateTime dateTime : dates) {
			String dateString = dateTime.toString("MM/dd/yyyy");
			
			Long dateCount = datesWithCount.get(dateString);
			if (dateCount != null){
				datesWithCount.put(dateString, dateCount+1);
			} else {
				datesWithCount.put(dateString, 1L);
			}
		}
		
		List<String> lineToWrite = Lists.newArrayList();
		for (Map.Entry<String, Long> entry : datesWithCount.entrySet()) {
			lineToWrite.add(entry.getKey() + "," + entry.getValue());
		}

		FileUtils.writeLines(fileToWrite, "UTF-8", lineToWrite, true);

		return fileToWrite;
	}

}
