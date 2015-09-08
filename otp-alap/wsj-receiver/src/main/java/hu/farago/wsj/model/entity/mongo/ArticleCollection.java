package hu.farago.wsj.model.entity.mongo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article")
public class ArticleCollection {

	@Id
	private Long id;
	private String rawText;
	private String plainText;
	private String title;
	private Date dateTime;
	private Date dateDay;
	private String url;
	private Boolean processed = false;
	private Set<String> articleWords = new HashSet<String>();

	public ArticleCollection() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
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

	public Date getDateDay() {
		return dateDay;
	}

	public void setDateDay(Date dateDay) {
		this.dateDay = dateDay;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

	public Set<String> getArticleWords() {
		return articleWords;
	}

	public void setArticleWords(Set<String> articleWords) {
		this.articleWords = articleWords;
	}

	@Override
	public String toString() {
		return "ArticleCollection [id=" + id + ", title=" + title
				+ ", dateTime=" + dateTime + ", url=" + url + "]";
	}
	
	

}
