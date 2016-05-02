package hu.farago.data.stooq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hu.farago.data.api.dto.HistoricalForexData;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.stooq.CsvFileToHistoricalDataConverter;
import hu.farago.data.utils.DateTimeUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CsvFileToHistoricalDataConverterTest extends AbstractRootTest {

	private static final String DATE_2010_05_21 = "2010-05-21";

	private static final String USDHUF_PATH = "usdhuf_d.csv";

	private File file;

	@Autowired
	private CsvFileToHistoricalDataConverter converter;

	@Before
	public void setUp() throws Exception {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(USDHUF_PATH);
		file = new File(url.getFile());
	}

	@Test
	public void fileExists() {
		assertNotNull(file);
	}

	@Test
	public void convertTest() throws IOException, ParseException {

		List<HistoricalForexData> data = converter.convert(file);
		assertNotNull(data);
		assertEquals(8, data.size());

		HistoricalForexData firstDataRow = new HistoricalForexData(
				DateTimeUtils.parseToYYYYMMDD_UTC(DATE_2010_05_21), 221.43, 224.71,
				220.89, 222.27);

		assertEquals(firstDataRow, data.get(0));
	}

}
