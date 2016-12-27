package hu.farago.data.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtils {

	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static final DateTimeFormatter FORMATTER2 = DateTimeFormat.forPattern("yyyyMMdd");
	private static final DateTimeFormatter FORMATTER3 = DateTimeFormat.forPattern("MM/dd/yyyy");
	private static final DateTimeFormatter FORMATTER4 = DateTimeFormat.forPattern("yyyy-MM");
	private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter FORMATTER_TIME_ZONE = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z");
	private static final DateTimeFormatter FORMATTER_TIME_T = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
	private static final DateTimeFormatter FORMATTER_FULL_MONTH = DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(Locale.US);

	public static DecimalFormat simpleNumberFormat;
	public static DecimalFormat dollarNumberFormat;
	
	static {
		NumberFormat nfSimple = NumberFormat.getNumberInstance(Locale.US);
		simpleNumberFormat = (DecimalFormat) nfSimple;
		simpleNumberFormat.applyPattern("###,###,###,###,###,###");

		NumberFormat nfDollar = NumberFormat.getNumberInstance(Locale.US);
		dollarNumberFormat = (DecimalFormat) nfDollar;
		dollarNumberFormat.applyPattern("$###,###,###,###,###,##0.####");
	}
	
	public static DateTime parseToYYYYMMDD_UTC(String dateTime) {
		return FORMATTER.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.UTC);
	}
	
	public static DateTime parseToYYYYMMDD_HHmmss_ZONE_UTC(String dateTime) {
		return FORMATTER_TIME_ZONE.parseDateTime(dateTime).withZone(DateTimeZone.UTC);
	}
	
	public static DateTime parseToYYYYMMDD_HHmmss_UTC(String dateTime) {
		return FORMATTER_TIME.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.UTC);
	}
	
	public static String formatToYYYYMMDD_HHmmss(DateTime dateTime) {
		return FORMATTER_TIME.print(dateTime);
	}
	
	public static DateTime parseToYYYYMMDD_withoutDashes_UTC(String dateTime) {
		return FORMATTER2.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.UTC);
	}
	
	public static DateTime parseToYYYYMMDD_HHmmss_ZONE(String dateTime) {
		return FORMATTER_TIME_T.parseDateTime(dateTime);
	}
	
	public static String formatToYYYYMMDDWithoutDashes(DateTime dt) {
		return FORMATTER2.print(dt);
	}
	
	public static DateTime parseToMMDDYYYY_UTC(String dateTime) throws Exception {
		return FORMATTER3.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.UTC);
	}
	
	public static DateTime parseToYYYYMM_UTC(String dateTime) {
		return FORMATTER4.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.UTC);
	}
	
	public static String formatToYYYYMM(DateTime dt) {
		return FORMATTER4.print(dt);
	}
	
	public static DateTime parseToDDMMMMYYYY_UTC(String dateTime) {
		return FORMATTER_FULL_MONTH.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.UTC);
	}
}
