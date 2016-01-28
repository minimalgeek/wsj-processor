package hu.farago.data.zacks;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import hu.farago.data.config.AbstractRootTest;
import hu.farago.data.zacks.dto.ZacksData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;

public class ZacksFileUtilsTest extends AbstractRootTest {

	private static DateTime currentTime;

	private static final String SAMPLE_PATH = "/csv_samples";
	private static final String SAMPLE_EXPECTED_PATH = "/csv_samples_expected";

	@Autowired
	private ZacksFileUtils zacksFileUtils;
	@Value("${zacks.path}")
	private String zacksPath;

	private File zacksDirectory;
	private ZacksData zacksDataSample;

	@BeforeClass
	public static void beforeClass() throws IOException {
		currentTime = DateTime.now();
		DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2015-10-25")
				.getMillis());
	}

	@AfterClass
	public static void afterClass() throws IOException {
		DateTimeUtils.setCurrentMillisFixed(currentTime.getMillis());
	}

	@SuppressWarnings("unchecked")
	@Before
	public void before() {

		zacksDirectory = new File(zacksPath);
		try {
			File srcDir = new File(getClass().getResource(SAMPLE_PATH)
					.getFile());
			FileUtils.copyDirectory(srcDir, zacksDirectory, true);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		List<String> cd3 = Lists.newArrayList();
		cd3.add("AAPL");
		cd3.add("01/20/16");

		List<String> cd4 = Lists.newArrayList();
		cd4.add("ISTC");
		cd4.add("10/25/15");

		List<String> cd2 = Lists.newArrayList();
		cd2.add("WWD");
		cd2.add("11/10/15");

		List<String> cd1 = Lists.newArrayList();
		cd1.add("ZBRA");
		cd1.add("10/10/15");

		zacksDataSample = new ZacksData();
		zacksDataSample.setData(Lists.newArrayList(cd1, cd2, cd3, cd4));
	}

	@After
	public void after() {
		try {
			FileUtils.cleanDirectory(zacksDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWriteZacksDataToCSVFiles() throws IOException {
		zacksFileUtils.writeZacksDataToCSVFiles(zacksDataSample);
		File expectedDirectory = new File(getClass().getResource(
				SAMPLE_EXPECTED_PATH).getFile());
		
		DirCompare compare = new DirCompare();
		compare.getDiff(expectedDirectory, zacksDirectory);

		assertThat(compare.getDifferentEntries(), hasSize(0));
	}

}
