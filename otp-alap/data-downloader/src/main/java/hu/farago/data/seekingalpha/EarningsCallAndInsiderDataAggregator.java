package hu.farago.data.seekingalpha;

import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.model.entity.mongo.InsiderData;
import hu.farago.data.model.entity.mongo.InsiderData.BuySell;
import hu.farago.data.model.entity.mongo.InsiderData.OwnerRelationShip;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class EarningsCallAndInsiderDataAggregator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EarningsCallAndInsiderDataAggregator.class);
	
	public void processCall(EarningsCall call, EarningsCall previousCall, List<InsiderData> insiderDataList) {
		DateTime from = previousCall.publishDate;
		DateTime to = call.publishDate;
		
		double buySum = 0.0, sellSum = 0.0;

		Map<String, List<InsiderData>> actorTradeMap = Maps.newHashMap();
		for (InsiderData data : insiderDataList) {
			DateTime acceptanceDate = data.acceptanceDate;
			
			if (acceptanceDate.isAfter(from) && acceptanceDate.isBefore(to)) {
				if (actorTradeMap.containsKey(data.reportingOwnerName)) {
					actorTradeMap.get(data.reportingOwnerName).add(data);
				} else {
					actorTradeMap.put(data.reportingOwnerName, Lists.newArrayList(data));
				}
				
				if (isRelevant(data)) {
					if (data.type == BuySell.BUY) {
						buySum += data.transactionShares;
					} else if (data.type == BuySell.SELL) {
						sellSum += data.transactionShares;
					}
				}
			}
		}
		
		double sumOfSharesOwned = calcSumOfSharesOwnedAtEndOfPeriod(actorTradeMap);
		
		call.sumOfBuyTransactionShares = buySum;
		call.sumOfSellTransactionShares = sellSum;
		call.sumOfSharesOwnedBeforePublishDate = sumOfSharesOwned;
	}

	private double calcSumOfSharesOwnedAtEndOfPeriod(
			Map<String, List<InsiderData>> actorTradeMap) {
		double sumOfSharesOwned = 0.0;
		
		for (Map.Entry<String, List<InsiderData>> entry : actorTradeMap.entrySet()) {
			DateTime maxTime = entry.getValue().stream().map(u -> u.acceptanceDate).max(DateTime::compareTo).get();
			InsiderData foundInsiderData = Iterables.find(entry.getValue(), new Predicate<InsiderData>() {

				@Override
				public boolean apply(InsiderData input) {
					return input.acceptanceDate.equals(maxTime);
				}
				
			});
			
			if(foundInsiderData != null) {
				sumOfSharesOwned += foundInsiderData.sharesOwned;
				LOGGER.info(
						"Increment shares => " + 
						foundInsiderData.reportingOwnerName + 
						" : " + 
						foundInsiderData.sharesOwned);
			}
		}
		return sumOfSharesOwned;
	}
	
	private boolean isRelevant(InsiderData data) {
		return data.ownerRelationShip == OwnerRelationShip.DIRECTOR || 
			   data.ownerRelationShip == OwnerRelationShip.DIRECTOR_OFFICER ||
			   data.ownerRelationShip == OwnerRelationShip.OFFICER;
	}
}
