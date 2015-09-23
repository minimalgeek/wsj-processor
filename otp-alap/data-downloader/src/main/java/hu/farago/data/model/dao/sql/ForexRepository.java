package hu.farago.data.model.dao.sql;

import hu.farago.data.model.entity.sql.Forex;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ForexRepository extends PagingAndSortingRepository<Forex, Long> {

	@Query("select max(f.tickDate) from hu.farago.data.model.entity.sql.Forex f "
			+ "where f.symbol = :symbol")
	Date findLatestDateForSymbol(@Param("symbol") String symbol);
	
}
