package hu.farago.data.download.stooq;

import hu.farago.data.api.dto.HistoricalForexData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class CsvFileToHistoricalDataConverter {
	
	private static final SimpleDateFormat csvDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public List<HistoricalForexData> convert(File file) throws IOException, ParseException {
		List<HistoricalForexData> retList = Lists.newArrayList();
		
		List<String> lines = FileUtils.readLines(file, "UTF-8");
		for (int i = 1; i < lines.size(); i++) {
			// Date,Open,High,Low,Close
			String[] tokens = StringUtils.split(lines.get(i), ',');
			
			HistoricalForexData data = new HistoricalForexData();
			data.setDate(getDateFromString(tokens[0]));
			data.setOpen(Double.valueOf(tokens[1]));
			data.setHigh(Double.valueOf(tokens[2]));
			data.setLow(Double.valueOf(tokens[3]));
			data.setClose(Double.valueOf(tokens[4]));
			
			retList.add(data);
		}
		
		return retList;
	}
	
	public Date getDateFromString(String dateStr) throws ParseException {
		return csvDateFormat.parse(dateStr);
	}
	
}
