package hu.farago.wsj.model.entity.sql;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "ARTICLE_WORD")
@DynamicUpdate
public class ArticleWord implements Serializable {

	private static final long serialVersionUID = -3596156191599654041L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "WORD")
	private String word;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ARTICLE_ID")
	private Article article;

	public ArticleWord() {
	}
	
	
	public ArticleWord(String word) {
		this.word = word;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
	
	

}
