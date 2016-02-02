package hu.farago.data.seekingalpha;

import hu.farago.data.model.dao.mongo.HarvardWordsRepository;
import hu.farago.data.model.dao.mongo.HenryWordsRepository;
import hu.farago.data.model.entity.mongo.EarningsCall;
import hu.farago.data.model.entity.mongo.HarvardWords;
import hu.farago.data.model.entity.mongo.HenryWords;
import hu.farago.data.seekingalpha.dto.HTone;
import hu.farago.data.seekingalpha.dto.Tone;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class ToneCalculator {

	@Autowired
	private HarvardWordsRepository harvardWR;
	
	@Autowired
	private HenryWordsRepository henryWR;
	
	private Map<String, HarvardWords> harvardMap;
	private Map<String, HenryWords> henryMap;
	
	@PostConstruct
	private void buildDictionary() {
		harvardMap = Maps.newHashMap();
		henryMap = Maps.newHashMap();
		
		List<HarvardWords> harvardWords = harvardWR.findAll();
		
		for (HarvardWords harvardWord : harvardWords) {
			String word = harvardWord.getRealEntry();
			
			if (!harvardMap.containsKey(word)) {
				harvardMap.put(word, harvardWord);
				continue;
			}
			
			HarvardWords mapValue = harvardMap.get(word);
			
			if (harvardWord.isActive()) {
				mapValue.active = "X";
			}
			
			if (harvardWord.isNegative()) {
				mapValue.negativ = "X";
			}
			
			if (harvardWord.isOverstated()) {
				mapValue.overstated = "X";
			}
			
			if (harvardWord.isPassive()) {
				mapValue.passive = "X";
			}
			
			if (harvardWord.isPositive()) {
				mapValue.positiv = "X";
			}
			
			if (harvardWord.isStrong()) {
				mapValue.strong = "X";
			}
			
			if (harvardWord.isUnderstated()) {
				mapValue.understated = "X";
			}
			
			if (harvardWord.isWeak()) {
				mapValue.weak = "X";
			}
		}
		
		List<HenryWords> henryWords = henryWR.findAll();
		
		for (HenryWords henryWord : henryWords) {
			henryMap.put(henryWord.getRealWord(), henryWord);
		}
	}
	
	public Tone getToneOf(EarningsCall call) {
		Tone tone = new Tone();
		
		for (String word : call.words) {
			if (harvardMap.containsKey(word)) {
				HarvardWords harvardWord = harvardMap.get(word);
				
				tone.activeCount += (harvardWord.isActive() ? 1 : 0);
				tone.passiveCount += (harvardWord.isPassive() ? 1 : 0);
				
				tone.negativeCount += (harvardWord.isNegative() ? 1 : 0);
				tone.positiveCount += (harvardWord.isPositive() ? 1 : 0);
				
				tone.strongCount += (harvardWord.isStrong() ? 1 : 0);
				tone.weakCount += (harvardWord.isWeak() ? 1 : 0);
				
				tone.overstatedCount += (harvardWord.isOverstated() ? 1 : 0);
				tone.understatedCount += (harvardWord.isUnderstated() ? 1 : 0);
			}
		}
		
		return tone;
	}

	public HTone getHToneOf(EarningsCall call) {
		HTone tone = new HTone();
		
		for (String word : call.words) {
			if (henryMap.containsKey(word)) {
				HenryWords henryWord = henryMap.get(word);
				
				tone.negativeCount += (henryWord.isNegative() ? 1 : 0);
				tone.positiveCount += (henryWord.isPositive() ? 1 : 0);
			}
		}
		
		return tone;
	}

}
