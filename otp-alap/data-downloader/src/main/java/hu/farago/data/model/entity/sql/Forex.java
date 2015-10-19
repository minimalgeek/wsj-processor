package hu.farago.data.model.entity.sql;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "FOREX")
@DynamicUpdate
public class Forex implements Serializable {

	private static final long serialVersionUID = 1953451328288625119L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "SYMBOL", nullable = false, columnDefinition="VARCHAR(50)")
	private String symbol;
	@Column(name = "TICK_DATE", nullable = false, columnDefinition="DATETIME")
	private Date tickDate;
	@Column(name="OPEN", nullable = false, columnDefinition="DOUBLE")
	private Double open;
	@Column(name="HIGH", nullable = true, columnDefinition="DOUBLE")
	private Double high;
	@Column(name="LOW", nullable = true, columnDefinition="DOUBLE")
	private Double low;
	@Column(name="CLOSE", nullable = true, columnDefinition="DOUBLE")
	private Double close;
	@Column(name="VOLUME", columnDefinition="DOUBLE")
	private Double volume;
	
	public Forex() {
	}
	
	public Forex(String symbol, Date tickDate, Double open, Double high,
			Double low, Double close, Double volume) {
		super();
		this.symbol = symbol;
		this.tickDate = tickDate;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Date getTickDate() {
		return tickDate;
	}

	public void setTickDate(Date tickDate) {
		this.tickDate = tickDate;
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

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
}
