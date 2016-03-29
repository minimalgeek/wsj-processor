package hu.farago.data.model.entity.mongo;

import java.io.Serializable;
import java.math.BigInteger;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "s_and_p_index")
public class SAndPIndex implements Serializable {

	private static final long serialVersionUID = -8221133662715524079L;

	@Id
	public BigInteger id;
	public String company;
	public String index;
	public IndexGroup indexGroup;
	public Operation lastOperation;
	public DateTime lastOperationDate;
	
	public static enum Operation {
		ADDED, DELETED;
	}
	
	public static enum IndexGroup {
		SP100, SP200, SP300, SP400, SP500, SP600;
	}
}
