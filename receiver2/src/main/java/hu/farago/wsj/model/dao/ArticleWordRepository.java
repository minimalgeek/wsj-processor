package hu.farago.wsj.model.dao;

import hu.farago.wsj.model.entity.ArticleWord;
import org.springframework.data.repository.CrudRepository;

public interface ArticleWordRepository extends CrudRepository<ArticleWord, Long> {

}
