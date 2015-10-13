package hu.farago.wsj.controller.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

public class CompanyInfoDTO implements Serializable {

	private static final long serialVersionUID = -6414246835037695155L;

	private String index;
	private List<String> searchTerms;

	public CompanyInfoDTO(String index, String... names) {
		this.index = index;
		this.searchTerms = Lists.newArrayList(names);
	}
	
	public CompanyInfoDTO(String index, List<String> names) {
		this.index = index;
		this.searchTerms = names;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<String> getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(List<String> searchTerms) {
		this.searchTerms = searchTerms;
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
