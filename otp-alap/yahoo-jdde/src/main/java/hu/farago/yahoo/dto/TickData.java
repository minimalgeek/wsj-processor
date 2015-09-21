package hu.farago.yahoo.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class TickData {

	private String currencyPair;
	
	private Double open;
	private Double high;
	private Double low;
	private Long volume;
	private Date time;
	private Double last;

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

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	public Double getLast() {
		return last;
	}
	
	public void setLast(Double last) {
		this.last = last;
	}

	public String getCurrencyPair() {
		return currencyPair;
	}
	
	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}
	
}
