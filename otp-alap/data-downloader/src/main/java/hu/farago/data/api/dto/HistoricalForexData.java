package hu.farago.data.api.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.joda.time.DateTime;

public class HistoricalForexData {

	private DateTime date;
	private Double open;
	private Double high;
	private Double low;
	private Double close;

	public HistoricalForexData() {
		super();
	}

	public HistoricalForexData(DateTime date, Double open, Double high, Double low,
			Double close) {
		super();
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		HistoricalForexData data = (HistoricalForexData) obj;

		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.open, data.open);
		builder.append(this.close, data.close);
		builder.append(this.high, data.high);
		builder.append(this.low, data.low);
		builder.append(this.date, data.date);
		return builder.isEquals();
	}
}
