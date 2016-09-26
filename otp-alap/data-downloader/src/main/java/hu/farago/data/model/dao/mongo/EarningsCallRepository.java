package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.EarningsCall;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EarningsCallRepository extends MongoRepository<EarningsCall, BigInteger> {
	
	List<EarningsCall> findByTradingSymbol(String tradingSymbol);
	
	List<EarningsCall> findByTradingSymbol(String tradingSymbol, Sort sort);
	
	EarningsCall findByUrl(String url);
	
	Long deleteByUrl(String url);
	
}