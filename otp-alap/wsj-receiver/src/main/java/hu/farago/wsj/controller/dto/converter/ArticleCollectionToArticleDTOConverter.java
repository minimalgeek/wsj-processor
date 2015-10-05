package hu.farago.wsj.controller.dto.converter;

import hu.farago.wsj.api.Converter;
import hu.farago.wsj.controller.dto.ArticleDTO;
import hu.farago.wsj.model.entity.mongo.ArticleCollection;

import org.springframework.stereotype.Component;

@Component("mongoConverter")
public class ArticleCollectionToArticleDTOConverter implements  Converter<ArticleCollection, ArticleDTO> {
	
	@Override
	public ArticleCollection convertFrom(ArticleDTO obj) {
		return new ArticleCollection(obj.getRawText(), obj.getTitle(), obj.getDateTime(),
				obj.getUrl());
	}

	@Override
	public ArticleDTO convertTo(ArticleCollection obj) {
		return new ArticleDTO(obj.getRawText(), obj.getTitle(),
				obj.getDateTime(), obj.getUrl());
	}
	

}
