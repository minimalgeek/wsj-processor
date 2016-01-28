package hu.farago.data.zacks.dto;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DoubleSerializer extends JsonDeserializer<Double> {

	private static Locale locale = new Locale("en", "US");
	private static String pattern = "###,###,###,###,###.##";
	private static DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
			.getNumberInstance(locale);
	{
		decimalFormat.applyPattern(pattern);
	}

	@Override
	public Double deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		try {
			Double d = Double.valueOf(jp.getText());
			return d;
		} catch (NumberFormatException nfe) {
			try {
				return decimalFormat.parse(jp.getText()).doubleValue();
			} catch (ParseException e) {
				return 0.0;
			}
		}
	}

}