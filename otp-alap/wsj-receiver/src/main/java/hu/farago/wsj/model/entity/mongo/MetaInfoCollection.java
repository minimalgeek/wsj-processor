package hu.farago.wsj.model.entity.mongo;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "metainfo")
public class MetaInfoCollection {

	public enum KEYS {
		LATESTDATE("latestDate");

		private String keyName;

		private KEYS(String keyName) {
			this.keyName = keyName;
		}

		public String getKeyName() {
			return keyName;
		}
	}

	@Id
	private BigInteger id;
	private String key;
	private Object value;

	public MetaInfoCollection() {
	}

	public MetaInfoCollection(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public MetaInfoCollection(KEYS key, Object value) {
		this.key = key.getKeyName();
		this.value = value;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
