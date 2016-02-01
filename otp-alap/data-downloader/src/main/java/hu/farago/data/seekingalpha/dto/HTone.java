package hu.farago.data.seekingalpha.dto;

import hu.farago.data.model.entity.mongo.EarningsCall;

public class HTone {

	public EarningsCall call;
	
	public int positiveCount;
	public int negativeCount;
	
	// tone 1
	
	public double getToneOnePN() {
		return positiveCount / negativeCount;
	}
	
	// tone 2
	
	public double getToneTwoPN() {
		return (positiveCount - negativeCount) / (positiveCount + negativeCount);
	}
}
