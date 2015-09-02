package hu.farago.wsj.controller.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class ArticleDTO implements Serializable {

	private static final long serialVersionUID = 280343763407135325L;

	@NotNull
	private String rawText;
	@NotNull
	private String title;
	@NotNull
	private Date dateTime;
	@NotNull
	private String url;

	public ArticleDTO() {
	}

	public ArticleDTO(String rawText, String title, Date dateTime, String url) {
		super();
		this.rawText = rawText;
		this.title = title;
		this.dateTime = dateTime;
		this.url = url;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ArticleDTO [rawText=" + rawText + ", title=" + title
				+ ", dateTime=" + dateTime + ", url=" + url + "]";
	}

}
