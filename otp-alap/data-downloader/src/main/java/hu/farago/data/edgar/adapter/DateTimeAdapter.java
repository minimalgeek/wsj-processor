package hu.farago.data.edgar.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeAdapter extends XmlAdapter<String, DateTime> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	public DateTime unmarshal(String v) throws Exception {
		return DateTime.parse(v, FORMATTER).withZoneRetainFields(DateTimeZone.UTC);
	}

	public String marshal(DateTime v) throws Exception {
		return v.toString(FORMATTER);
	}

}