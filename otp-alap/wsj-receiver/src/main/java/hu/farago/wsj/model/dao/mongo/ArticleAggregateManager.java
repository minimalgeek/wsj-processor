package hu.farago.wsj.model.dao.mongo;

import hu.farago.wsj.controller.dto.CompanyInfoDTO;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Repository
public class ArticleAggregateManager {

	private static final Integer PAGE_SIZE = 50;

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<ArticleCollection> findAllByFullTextSearch(CompanyInfoDTO dto) {
		Map<BigInteger, ArticleCollection> retMap = Maps.newHashMap();
		for (String searchTerm : dto.getSearchTerms()) {
			TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm);
		
			boolean run = true;
			int pageIdx = 0;
	
			while (run) {
				Query query = TextQuery.queryText(criteria).with(
						new PageRequest(pageIdx, PAGE_SIZE));
				pageIdx++;
	
				List<ArticleCollection> foundArticles = mongoTemplate.find(query, ArticleCollection.class);
				if (!CollectionUtils.isEmpty(foundArticles)) {
					for (ArticleCollection collection : foundArticles) {
						retMap.put(collection.getId(), collection);
					}
				} else {
					run = false;
				}
			}
		}

		return Lists.newArrayList(retMap.values());
	}
}
