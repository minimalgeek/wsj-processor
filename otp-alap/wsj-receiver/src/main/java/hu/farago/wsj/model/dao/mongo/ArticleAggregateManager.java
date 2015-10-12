package hu.farago.wsj.model.dao.mongo;

import hu.farago.wsj.controller.dto.CompanyInfoDTO;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

@Repository
public class ArticleAggregateManager {

	private static final Integer PAGE_SIZE = 50;

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<ArticleCollection> findAllByFullTextSearch(CompanyInfoDTO dto) {
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(dto.getName()).matching(dto.getIndex());

		List<ArticleCollection> retList = Lists.newArrayList();
		boolean run = true;
		int pageIdx = 0;

		while (run) {
			Query query = TextQuery.queryText(criteria).with(
					new PageRequest(pageIdx, PAGE_SIZE));
			pageIdx++;

			List<ArticleCollection> foundArticles = mongoTemplate.find(query, ArticleCollection.class);
			if (!CollectionUtils.isEmpty(foundArticles)) {
				retList.addAll(foundArticles);
			} else {
				run = false;
			}
		}

		return retList;
	}
}
