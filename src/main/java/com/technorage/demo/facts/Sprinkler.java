package com.technorage.demo.facts;
public class Sprinkler {

    private RuleSetup ruleSetup;
    private OrderLine orderLine;

	public Sprinkler(RuleSetup ruleSetup) {
		this.ruleSetup = ruleSetup;
	}
	
	public Sprinkler(OrderLine orderLine) {
		this.orderLine = orderLine;
	}

	public RuleSetup getRuleSetup() {
		return ruleSetup;
	}

	public void setRuleSetup(RuleSetup ruleSetup) {
		this.ruleSetup = ruleSetup;
	}

	public OrderLine getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(OrderLine orderLine) {
		this.orderLine = orderLine;
	}

 
}