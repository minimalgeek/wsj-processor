package hu.farago.data.model.dao.sql;

import hu.farago.data.model.entity.sql.Forex;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ForexRepository extends PagingAndSortingRepository<Forex, Long> {
	
}
