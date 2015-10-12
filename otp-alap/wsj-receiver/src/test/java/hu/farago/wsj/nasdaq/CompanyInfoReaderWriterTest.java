package hu.farago.wsj.nasdaq;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import hu.farago.wsj.config.SimpleAbstractRootTest;
import hu.farago.wsj.controller.dto.CompanyInfoDTO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public class CompanyInfoReaderWriterTest extends SimpleAbstractRootTest {
	
	@Autowired 
	private CompanyInfoReaderWriter companyInfoReader;
	
	private CompanyInfoDTO aapl;
	private CompanyInfoDTO adbe;
	
	@Before
	public void before() {
		aapl = new CompanyInfoDTO("AAPL", "APPLE INC");
		adbe = new CompanyInfoDTO("ADBE", "ADOBE SYSTEMS INC");
	}

	@Test
	public void readAllTickersTest() throws IOException {
		List<CompanyInfoDTO> ret = companyInfoReader.readAllTickers();
		assertThat(ret, hasItems(aapl, adbe));
	}
	
	@Test
	public void writeRelevantDatesToFileTest() throws IOException {
		
		List<DateTime> dates = Lists.newArrayList();
		dates.add(DateTime.parse("2015-01-01"));
		dates.add(DateTime.parse("2015-03-14"));
		dates.add(DateTime.parse("2015-08-12"));

		File file = companyInfoReader.writeRelevantDatesToFile(aapl, dates);
		
		assertTrue(file.exists());
		assertThat(FileUtils.readLines(file), hasSize(3));
	}

}