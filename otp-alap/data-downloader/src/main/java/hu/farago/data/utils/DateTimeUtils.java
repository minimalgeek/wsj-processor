package hu.farago.data.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtils {

	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static final DateTimeFormatter FORMATTER2 = DateTimeFormat.forPattern("yyyyMMdd");
	
	public static DateTime parseToYYYYMMDD_UTC(String dateTime) {
		return FORMATTER.parseDateTime(dateTime).withZoneRetainFields(DateTimeZone.UTC);
	}
	
	public static String formatToYYYYMMDDWithoutDashes(DateTime dt) {
		return FORMATTER2.print(dt);
	}
	
}
