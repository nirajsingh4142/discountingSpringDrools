package com.technorage.demo.facts;
public class Sprinkler {

    private RuleSetup ruleSetup;
    private OrderLine orderLine;

    private boolean on;

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

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	@Override
	public String toString() {
		return "Sprinkler [ruleSetup=" + ruleSetup + ", on=" + on + "]";
	}

	public OrderLine getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(OrderLine orderLine) {
		this.orderLine = orderLine;
	}

 
}