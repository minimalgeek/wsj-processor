package hu.farago.zacks.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZacksData {

	private String title;
	private Boolean header;
	private List<CompanyData> data;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getHeader() {
		return header;
	}

	public void setHeader(Boolean header) {
		this.header = header;
	}

	public List<CompanyData> getData() {
		return data;
	}

	public void setData(List<CompanyData> data) {
		this.data = data;
	}

}
