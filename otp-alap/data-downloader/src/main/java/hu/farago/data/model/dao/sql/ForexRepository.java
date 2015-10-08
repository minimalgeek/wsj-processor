package hu.farago.data.model.dao.sql;

import hu.farago.data.model.entity.sql.Forex;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ForexRepository extends PagingAndSortingRepository<Forex, Long> {

	@Query("select max(f.tickDate) from hu.farago.data.model.entity.sql.Forex f "
			+ "where f.symbol = :symbol")
	Date findLatestDateForSymbol(@Param("symbol") String symbol);
	
	@Modifying
	@Transactional
	@Query("delete from hu.farago.data.model.entity.sql.Forex f "
			+ "where f.symbol = :symbol and f.tickDate = :date")
	void deleteByDateAndSymbol(@Param("date") Date date, @Param("symbol") String symbol);
}
