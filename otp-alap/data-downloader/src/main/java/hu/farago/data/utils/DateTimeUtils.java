package hu.farago.data.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtils {

	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static final DateTimeFormatter FORMATTER2 = DateTimeFormat.forPattern("yyyyMMdd");
	private static final DateTimeFormatter FORMATTER3 = DateTimeFormat.forPattern("MM/dd/yyyy");
	private static final DateTimeFormatter FORMATTER4 = DateTimeFormat.forPattern("yyyy-MM");
	
	public static DateTime parseToYYYYMMDD_UTC(String dateTime) {
		return FORMATTER.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.UTC);
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
}
