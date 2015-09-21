package hu.farago.yahoo.download;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertyLoader {
	
	private Result result;
	private InputStream inputStream;
	
	public Result getPropValues() throws IOException {
		return getPropValues("application.properties");
	}

	public Result getPropValues(String fileName) throws IOException {

		try {
			
			Properties prop = new Properties();
			
			inputStream = getClass().getClassLoader().getResourceAsStream(
					fileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"
						+ fileName + "' not found in the classpath");
			}
			
			result = new Result();
			result.currencyPairs = Arrays.asList(StringUtils.split(prop.getProperty("currency.pairs"), ','));
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		
		return result;
	}
	
	public Result getResult() {
		return result;
	}

	public static class Result {
		public List<String> currencyPairs;
	}
}