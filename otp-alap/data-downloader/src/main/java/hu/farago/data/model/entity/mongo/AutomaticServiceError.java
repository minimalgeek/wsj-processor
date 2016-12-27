package hu.farago.data.model.entity.mongo;

import java.io.Serializable;
import java.math.BigInteger;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import hu.farago.data.utils.DateTimeUtils;

@Document(collection = "automatic_service_error")
public class AutomaticServiceError implements Serializable {

	public enum AutomaticService {
		STOOQ, YAHOO, SPICE_INDICES, SEEKING_ALPHA, ZACKS, MONGO_FLAT
	}

	private static final long serialVersionUID = -7979032736517713670L;

	@Id
	private BigInteger id;
	private DateTime dateTime;
	private AutomaticService automaticService;
	private String message;

	public AutomaticServiceError(DateTime dateTime, AutomaticService automaticService, String message) {
		super();
		this.dateTime = dateTime;
		this.automaticService = automaticService;
		this.message = message;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public AutomaticService getAutomaticService() {
		return automaticService;
	}

	public void setAutomaticService(AutomaticService automaticService) {
		this.automaticService = automaticService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getDateTimeString() {
		return DateTimeUtils.formatToYYYYMMDD_HHmmss(dateTime);
	}

}
