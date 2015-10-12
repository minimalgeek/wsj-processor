package hu.farago.wsj.controller.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CompanyInfoDTO implements Serializable {
	
	private static final long serialVersionUID = -6414246835037695155L;

	private String index;
	private String name;
	
	public CompanyInfoDTO() {
	}
	
	public CompanyInfoDTO(String index, String name) {
		this.index = index;
		this.name = name;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
