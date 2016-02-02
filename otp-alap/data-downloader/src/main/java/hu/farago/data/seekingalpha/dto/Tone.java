package hu.farago.data.seekingalpha.dto;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Tone {
	
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
		return (double)positiveCount / (double)negativeCount;
	}
	
	public double getToneOneAP() {
		return (double)activeCount / (double)passiveCount;
	}
	
	public double getToneOneSW() {
		return (double)strongCount / (double)weakCount;
	}
	
	public double getToneOneOU() {
		return (double)overstatedCount / (double)understatedCount;
	}
	
	// tone 2
	
	public double getToneTwoPN() {
		return (double)(positiveCount - negativeCount) / (double)(positiveCount + negativeCount);
	}
	
	public double getToneTwoAP() {
		return (double)(activeCount - passiveCount) / (double)(activeCount + passiveCount);
	}
	
	public double getToneTwoSW() {
		return (double)(strongCount - weakCount) / (double)(strongCount + weakCount);
	}
	
	public double getToneTwoOU() {
		return (double)(overstatedCount - understatedCount) / (double)(overstatedCount + understatedCount);
	}
}
