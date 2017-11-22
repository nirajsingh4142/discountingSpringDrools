package com.technorage.demo.services;

import java.util.Collection;

import com.technorage.demo.facts.Alarm;
import com.technorage.demo.facts.OrderSprinkler;
import com.technorage.demo.facts.RuleSetup;
import com.technorage.demo.facts.Sprinkler;
import com.technorage.demo.facts.StandardRuleSetup;
import com.technorage.demo.facts.StandardSprinkler;
import com.technorage.demo.forms.DemoForm;


public interface DemoRuleService<T> {
	Collection<Alarm> checkForFire();
	Collection<RuleSetup> generateOffer(DemoForm demoForm);
	Collection<StandardRuleSetup> getStandardRulesQualified(DemoForm demoForm);
	Collection<Sprinkler> checkSprinklers();
	Collection<OrderSprinkler> getOrderLines();
	Collection<StandardSprinkler> checkStandardSprinklers();

	void addRule(DemoForm demoForm);
	void addOrder(DemoForm demoForm);
	void addStandardRule(DemoForm demoForm);
	void delOrderSprinklers();
	void disposeKiSession();
	
}
