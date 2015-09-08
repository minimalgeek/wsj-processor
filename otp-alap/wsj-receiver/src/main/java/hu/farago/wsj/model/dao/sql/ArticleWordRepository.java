package hu.farago.wsj.model.dao.sql;

import hu.farago.wsj.model.entity.sql.Article;
import hu.farago.wsj.model.entity.sql.ArticleWord;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ArticleWordRepository extends CrudRepository<ArticleWord, Long> {
	
	List<ArticleWord> findByArticle(Article a);

}
