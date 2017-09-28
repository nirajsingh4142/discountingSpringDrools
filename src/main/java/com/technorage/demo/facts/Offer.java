/**
 * 
 */
package com.technorage.demo.facts;

import java.util.ArrayList;
import java.util.List;

/**
 * @author raghav.rampal
 *
 */
public class Offer {

	private Boolean hardcode = false;
	
	private Boolean overridenExplicitly = false;
	
	private Integer priority;
	private Integer comboField;
	
	private List<Terms> terms = new ArrayList<Terms>();
	public Offer() {
		
	}

	public Boolean getHardcode() {
		return hardcode;
	}

	public void setHardcode(Boolean hardcode) {
		this.hardcode = hardcode;
	}

	public Boolean getOverridenExplicitly() {
		return overridenExplicitly;
	}

	public void setOverridenExplicitly(Boolean overridenExplicitly) {
		this.overridenExplicitly = overridenExplicitly;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public List<Terms> getTerms() {
		return terms;
	}

	public void setTerms(List<Terms> terms) {
		this.terms = terms;
	}

	public Integer getComboField() {
		return comboField;
	}

	public void setComboField(Integer comboField) {
		this.comboField = comboField;
	}

	
	
	
}
