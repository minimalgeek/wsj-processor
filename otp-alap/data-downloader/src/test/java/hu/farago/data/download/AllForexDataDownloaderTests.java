package hu.farago.data.download;

import hu.farago.data.download.stooq.CsvFileToHistoricalDataConverterTest;
import hu.farago.data.download.stooq.StooqDataDownloaderTest;
import hu.farago.data.download.yahoo.YahooCurrencyPairDownloaderTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({YahooCurrencyPairDownloaderTest.class, StooqDataDownloaderTest.class, CsvFileToHistoricalDataConverterTest.class})
public class AllForexDataDownloaderTests {

}
