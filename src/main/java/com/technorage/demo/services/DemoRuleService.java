package com.technorage.demo.services;

import java.util.Collection;

import com.technorage.demo.facts.Alarm;
import com.technorage.demo.facts.OrderSprinkler;
import com.technorage.demo.facts.Room;
import com.technorage.demo.facts.RuleSetup;
import com.technorage.demo.facts.Sprinkler;
import com.technorage.demo.facts.StandardRuleSetup;
import com.technorage.demo.facts.StandardSprinkler;
import com.technorage.demo.forms.DemoForm;


public interface DemoRuleService<T> {

    Collection<Alarm>  addFire(String[] fires) ;
    
    Collection<Alarm>  remFire(String[]  fires) ;

	//void addRooms(String[] names);
	
	Collection<Alarm> checkForFire();
    
	Room getRoom(String name);

	void addRoom(DemoForm demoForm);
	
	Collection<RuleSetup> generateOffer(DemoForm demoForm);
	
	Collection<StandardRuleSetup> getStandardRulesQualified(DemoForm demoForm);
	
	void addOrder(DemoForm demoForm);

	Collection<Sprinkler> checkSprinklers();
	
	Collection<OrderSprinkler> checkOrderSprinklers();
	
	Collection<StandardSprinkler> checkStandardSprinklers();

	void addStandardRule(DemoForm demoForm);
    
	void delOrderSprinklers();
	void disposeKiSession();
}
