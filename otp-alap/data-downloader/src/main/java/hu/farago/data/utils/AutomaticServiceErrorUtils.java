package hu.farago.data.utils;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.farago.data.model.dao.mongo.AutomaticServiceErrorRepository;
import hu.farago.data.model.entity.mongo.AutomaticServiceError;
import hu.farago.data.model.entity.mongo.AutomaticServiceError.AutomaticService;

@Component
public class AutomaticServiceErrorUtils {

	@Autowired
	private AutomaticServiceErrorRepository repo;
	
	public void saveError(AutomaticService service, String error) {
		repo.save(new AutomaticServiceError(DateTime.now(), service, error));
	}
	
}
