package hu.farago.wsj.model.dao;

import hu.farago.wsj.model.entity.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {

	@Query("select a from hu.farago.wsj.model.entity.Article a "
			+ "left join fetch a.articleWords words "
			+ "where a.url = :url")
	Article findByUrlAndFetchWords(@Param("url") String url);
	
	@Query("select a from hu.farago.wsj.model.entity.Article a "
			+ "where a.processed = false")
	Page<Article> findAllUnProcessed(Pageable pageable); 
	
	@Query("select count(a.id) from hu.farago.wsj.model.entity.Article a "
			+ "where a.processed = false")
	long countAllUnProcessed(); 
	
}
