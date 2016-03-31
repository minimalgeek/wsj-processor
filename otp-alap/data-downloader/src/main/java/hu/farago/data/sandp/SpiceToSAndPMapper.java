package hu.farago.data.sandp;

import hu.farago.data.model.entity.mongo.SAndPIndex;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.Event;
import hu.farago.data.model.entity.mongo.embedded.SAndPOperation.SAndPGroup;
import hu.farago.data.sandp.dto.CompanyJSON;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

@Component
public class SpiceToSAndPMapper {

	public SAndPIndex map(CompanyJSON company) {
		SAndPIndex index = new SAndPIndex();
		index.company = company.currentCompanyName;
		index.tradingSymbol = company.currentTicker;

		SAndPOperation operation = new SAndPOperation();
		operation.event = Event.getByName(company.eventName);
		operation.eventDate = new DateTime(company.closeAsOfDate)
				.withZoneRetainFields(DateTimeZone.UTC).withTimeAtStartOfDay();
		operation.indexGroup = SAndPGroup.getByName(company.indexName);

		index.operations.add(operation);

		if (operation.event != null) {
			return index;
		}
		return null;
	}

}
