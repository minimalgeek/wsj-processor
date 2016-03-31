package hu.farago.data.model.entity.mongo;

import hu.farago.data.model.entity.mongo.embedded.SAndPOperation;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;

@Document(collection = "s_and_p_index")
public class SAndPIndex implements Serializable {

	private static final long serialVersionUID = -8221133662715524079L;

	@Id
	public BigInteger id;
	public String company;
	public String tradingSymbol;
	public List<SAndPOperation> operations = Lists.newArrayList();
	
	@Override
	public boolean equals(Object obj) {
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(this.tradingSymbol, ((SAndPIndex) obj).tradingSymbol);
		
		return eb.isEquals();
	}
	
	public void merge(SAndPIndex other) {
		root : for (SAndPOperation operation : other.operations) {
			for (SAndPOperation oldOperation : this.operations) {
				if (operation.equals(oldOperation)) {
					break root;
				}
			}
			
			this.operations.addAll(other.operations);
		}
	}
	
}
