package hu.farago.wsj.model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "ARTICLE")
@DynamicUpdate
public class Article {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "RAW_TEXT", nullable = false, columnDefinition="TEXT")
	private String rawText;
	
	@Column(name = "PLAIN_TEXT", columnDefinition="TEXT")
	private String plainText;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "DATE_TIME", nullable = false)
	private Date dateTime;

	@Column(name = "URL", nullable = false)
	private String url;
	
	@Column(name = "PROCESSED", nullable = true)
	private Boolean processed = false;
	
	@OneToMany(
			mappedBy="article", 
			cascade = {
				CascadeType.ALL
			}
	)
	private Set<ArticleWord> articleWords = new HashSet<ArticleWord>();

	public Article() {
	}

	public Article(String rawText, String title, Date dateTime, String url) {
		super();
		this.rawText = rawText;
		this.title = title;
		this.dateTime = dateTime;
		this.url = url;
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

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	public Set<ArticleWord> getArticleWords() {
		return articleWords;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

	public void setArticleWords(Set<ArticleWord> articleWords) {
		this.articleWords = articleWords;
	}
	
	public void addArticleWord(ArticleWord word) {
		this.articleWords.add(word);
		word.setArticle(this);
	}

}
