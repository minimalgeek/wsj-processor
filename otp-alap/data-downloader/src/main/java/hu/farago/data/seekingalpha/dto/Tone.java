package hu.farago.data.seekingalpha.dto;

import hu.farago.data.model.entity.mongo.EarningsCall;

public class Tone {

	public EarningsCall call;
	
	public int positiveCount;
	public int negativeCount;
	
	public int strongCount;
	public int weakCount;
	
	public int activeCount;
	public int passiveCount;
	
	public int overstatedCount;
	public int understatedCount;

	// tone 1
	
	public double getToneOnePN() {
		return positiveCount / negativeCount;
	}
	
	public double getToneOneAP() {
		return activeCount / passiveCount;
	}
	
	public double getToneOneSW() {
		return strongCount / weakCount;
	}
	
	public double getToneOneOU() {
		return overstatedCount / understatedCount;
	}
	
	// tone 2
	
	public double getToneTwoPN() {
		return (positiveCount - negativeCount) / (positiveCount + negativeCount);
	}
	
	public double getToneTwoAP() {
		return (activeCount - passiveCount) / (activeCount + passiveCount);
	}
	
	public double getToneTwoSW() {
		return (strongCount - weakCount) / (strongCount + weakCount);
	}
	
	public double getToneTwoOU() {
		return (overstatedCount - understatedCount) / (overstatedCount + understatedCount);
	}
}
