package hu.farago.mongo.model.entity.mongo;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "harvard_words")
public class HarvardWords {

	@Id
	public BigInteger id;
	@Field("Entry")
	public String entry;
	@Field("Positiv")
	public String positiv;
	@Field("Negativ")
	public String negativ;
	@Field("Strong")
	public String strong;
	@Field("Weak")
	public String weak;
	@Field("Active")
	public String active;
	@Field("Passive")
	public String passive;
	@Field("Ovrst")
	public String overstated;
	@Field("Undrst")
	public String understated;
	
	public String getRealEntry() {
		if (!StringUtils.contains(entry, "#")) {
			return StringUtils.lowerCase(entry);
		} else {
			return StringUtils.lowerCase(StringUtils.removePattern(entry, "#\\d*"));
		}
	}
	
	public boolean isPositive() {
		return StringUtils.isNotBlank(positiv);
	}
	
	public boolean isNegative() {
		return StringUtils.isNotBlank(negativ);
	}

	public boolean isStrong() {
		return StringUtils.isNotBlank(strong);
	}
	
	public boolean isWeak() {
		return StringUtils.isNotBlank(weak);
	}

	public boolean isActive() {
		return StringUtils.isNotBlank(active);
	}

	public boolean isPassive() {
		return StringUtils.isNotBlank(passive);
	}

	public boolean isOverstated() {
		return StringUtils.isNotBlank(overstated);
	}

	public boolean isUnderstated() {
		return StringUtils.isNotBlank(understated);
	}
}
