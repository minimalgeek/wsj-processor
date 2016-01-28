package hu.farago.data;

import hu.farago.data.stooq.CsvFileToHistoricalDataConverterTest;
import hu.farago.data.stooq.StooqDataDownloaderTest;
import hu.farago.data.yahoo.YahooCurrencyPairDownloaderTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({YahooCurrencyPairDownloaderTest.class, StooqDataDownloaderTest.class, CsvFileToHistoricalDataConverterTest.class})
public class AllForexDataDownloaderTests {

}
