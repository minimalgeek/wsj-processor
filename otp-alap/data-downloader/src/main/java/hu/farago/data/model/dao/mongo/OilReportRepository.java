package hu.farago.data.model.dao.mongo;

import hu.farago.data.model.entity.mongo.OilReport;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OilReportRepository extends MongoRepository<OilReport, BigInteger> {

}
