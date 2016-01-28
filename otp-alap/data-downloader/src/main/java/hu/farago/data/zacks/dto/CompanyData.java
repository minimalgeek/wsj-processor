package hu.farago.data.zacks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CompanyData {

	@JsonProperty("Symbol")
	private String symbol;
	@JsonProperty("Company")
	private String company;
	@JsonProperty("Last")
	@JsonDeserialize(using = DoubleSerializer.class)
	private Double last;
	@JsonProperty("Earnings ESP")
	private String earnings;
	@JsonProperty("Last FY Actual")
	@JsonDeserialize(using = DoubleSerializer.class)
	private Double lastFY;
	@JsonProperty("This FY Est")
	@JsonDeserialize(using = DoubleSerializer.class)
	private Double thisFY;
	@JsonProperty("Next FY Est")
	@JsonDeserialize(using = DoubleSerializer.class)
	private Double nextFY;
	@JsonProperty("Q1 Est")
	@JsonDeserialize(using = DoubleSerializer.class)
	private Double q1;
	@JsonProperty("YR/YR F1_Growth")
	@JsonDeserialize(using = DoubleSerializer.class)
	private Double f1Growth;
	@JsonProperty("Next Report Date")
	private String nextReportDate;
	@JsonProperty("LTG_%")
	@JsonDeserialize(using = DoubleSerializer.class)
	private Double ltg;
	@JsonProperty("Growth Score")
	private String growthScore;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Double getLast() {
		return last;
	}

	public void setLast(Double last) {
		this.last = last;
	}

	public String getEarnings() {
		return earnings;
	}

	public void setEarnings(String earnings) {
		this.earnings = earnings;
	}

	public Double getLastFY() {
		return lastFY;
	}

	public void setLastFY(Double lastFY) {
		this.lastFY = lastFY;
	}

	public Double getThisFY() {
		return thisFY;
	}

	public void setThisFY(Double thisFY) {
		this.thisFY = thisFY;
	}

	public Double getNextFY() {
		return nextFY;
	}

	public void setNextFY(Double nextFY) {
		this.nextFY = nextFY;
	}

	public Double getQ1() {
		return q1;
	}

	public void setQ1(Double q1) {
		this.q1 = q1;
	}

	public Double getF1Growth() {
		return f1Growth;
	}

	public void setF1Growth(Double f1Growth) {
		this.f1Growth = f1Growth;
	}

	public String getNextReportDate() {
		return nextReportDate;
	}

	public void setNextReportDate(String nextReportDate) {
		this.nextReportDate = nextReportDate;
	}

	public Double getLtg() {
		return ltg;
	}

	public void setLtg(Double ltg) {
		this.ltg = ltg;
	}

	public String getGrowthScore() {
		return growthScore;
	}

	public void setGrowthScore(String growthScore) {
		this.growthScore = growthScore;
	}

}
