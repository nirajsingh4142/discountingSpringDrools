package com.technorage.demo.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.technorage.demo.facts.Alarm;
import com.technorage.demo.facts.OrderSprinkler;
import com.technorage.demo.facts.RuleSetup;
import com.technorage.demo.facts.Sprinkler;
import com.technorage.demo.facts.StandardRuleSetup;
import com.technorage.demo.facts.StandardSprinkler;
import com.technorage.demo.forms.DemoForm;
import com.technorage.demo.forms.RuleSetupForm;
import com.technorage.demo.services.DemoRuleService;
import com.technorage.demo.utils.DateUtils;

@Controller
public class HomeControllerImpl implements HomeController {

	private static Logger logger = LoggerFactory.getLogger(HomeControllerImpl.class);

	@Autowired
	private DemoRuleService<?> ruleService;

	private Map<String, String> rooms = new LinkedHashMap<String,String>();
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Override
	public String index(Locale locale, Model model) {
		return getIndex(locale, model);
	}

	@Override
	public String addRoom(DemoForm demoForm, Locale locale, Model model) {
		logger.info("Adding rule: " + demoForm.getRuleName());
		ruleService.addRoom(demoForm);
		rooms.put(demoForm.getRoomName(), demoForm.getRuleName());

		return getIndex(locale, model);
	}

	@Override
	public String addOrder(DemoForm demoForm, Locale locale, Model model) {
		logger.info("Adding Order line: " + demoForm.getOrderLineNumber());
		ruleService.addOrder(demoForm);

		return getIndex(locale, model);
	}

	@Override
	public String generateOffer(DemoForm demoForm, Locale locale, Model model) {
		List<String> priorityList = new ArrayList<String>();
		List<RuleSetup> rulesQualified = new ArrayList<RuleSetup>();
		
		Collection<StandardRuleSetup> standardRulesQualified = ruleService.getStandardRulesQualified(null);
		Collection<RuleSetup> ruleSetupList = ruleService.generateOffer(null);
		
		Integer maxPriority = 0;
		Double totalDiscount = 0.0;
		String winnerRules = "";
		String terms = "";
		String winner = " ";
		String qualifierList = " ";
		int discount = 0;
		
		//fetch order line quantity
		int usedQty = 0;
		Collection<OrderSprinkler> orderSprinklers = ruleService.checkOrderSprinklers();
		for(OrderSprinkler lines : orderSprinklers) {
			usedQty = lines.getOrderLine().getQuantity();
		}
		
		//for case rule is qualified
		for(RuleSetup ruleSetup : ruleSetupList) {
			qualifierList = qualifierList + ", " + ruleSetup.getRuleName();
			priorityList.add(ruleSetup.getWinningPriority());
			rulesQualified.add(ruleSetup);

			if(ruleSetup.getOffer().getPriority() !=null && ruleSetup.getOffer().getPriority() > maxPriority) {
				maxPriority = ruleSetup.getOffer().getPriority();
			}
			
			discount = getDiscountOnBasisOfQty(usedQty, ruleSetup.getMap());
			if(discount == 0) {
				
				//case no standard rule exists
				if(ruleSetup.getDiscount()!=null && ruleSetup.getDiscount().getPercentage()!=null) {
					discount = ruleSetup.getDiscount().getPercentage().intValue();
				}

				//take discount from standard rule if exists
				for(StandardRuleSetup stdRule : standardRulesQualified) {
					discount = getDiscountOnBasisOfQty(usedQty, stdRule.getMap());
				}
				
			}
			
			winner = "  " + ruleSetup.getRuleName() + " wins with discount: " + discount + "%";

		}

		sortListOnBasisOfRule(rulesQualified);

		// standard rule will execute only when none of account rules is qualified
		if(rulesQualified.isEmpty()) {
			for(StandardRuleSetup setup : standardRulesQualified) {
				qualifierList = qualifierList + ", " + setup.getRuleName();
				discount = getDiscountOnBasisOfQty(usedQty, setup.getMap());
				winner = "  " +  setup.getRuleName() + " wins with discount: " + discount + "%";
			}
		}
		
		for(RuleSetup setup : rulesQualified) {
			//Display winner with combo field parameters excluded
			if(setup.getOffer().getComboField() == null) {
				/*if(setup.getWinningPriority().equals("P1")) {
					winner = winner + ", " + setup.getRuleName();
					System.out.println("Rule " + setup.getRuleNumber() + " wins with discount: " + setup.getDiscount().getPercentage() + "%");
					break;

				} else if(!priorityList.contains("P1") && setup.getWinningPriority().equals("P2")) {
					winner = winner + ", " + setup.getRuleName();
					System.out.println("Rule " + setup.getRuleNumber() + " wins with discount: " + setup.getDiscount().getPercentage() + "%");
					break;

				} else if(setup.getOffer().getPriority() == maxPriority) {
					winner = winner + ", " + setup.getRuleName();
					System.out.println("Rule " + setup.getRuleNumber() + " wins with discount: " + setup.getDiscount().getPercentage() + "%");
					break;
				} */
			} 

			//Display winner with combo field parameters included
			else if(setup.getIsWinner()) {
				if(setup.getDiscount()!=null && setup.getDiscount().getPercentage()!=null) {
					winner = winner + ", " + setup.getRuleName();
					totalDiscount = totalDiscount + setup.getDiscount().getPercentage();
					winnerRules = winnerRules + ", " + setup.getRuleNumber();
				}

				if(setup.getOffer().getDays() != null) {
					terms = " and Term " + setup.getOffer().getDays() + " days from Rule " + setup.getRuleNumber();
				}
				//if freight charge is false, it is free freight 
				if(setup.getOffer().getFrieghtCharge().equalsIgnoreCase("false")) {
					terms = "\n" + terms + "\n having free freight from Rule " + setup.getRuleNumber();
				}
			}

		}

		if(!winnerRules.isEmpty()) {
			winner = "  Rule " + winnerRules.substring(2) + " wins with discount: " + totalDiscount + "%" + terms;
		}

		Collection<Sprinkler> sprinklers = ruleService.checkSprinklers();
		Collection<StandardSprinkler> standardSprinklers=ruleService.checkStandardSprinklers();

		model.addAttribute("sprinklers", sprinklers);
		model.addAttribute("orderSprinklers", orderSprinklers);
		model.addAttribute("standardSprinklers", standardSprinklers);
	
		if(!winner.equals(" ")) {
			model.addAttribute("noOfAlarms", winner.substring(2));
		}
		if(!qualifierList.equals(" ")) {
			model.addAttribute("qualifiers", qualifierList.substring(2));
		}
		
		model.addAttribute("serverTime", DateUtils.getFormattedDate(locale) );

		return ("index");
	}

	@Override
	public String addFire(DemoForm demoForm,Locale locale, Model model) {
		if(demoForm.getFireRoomName()!=null && !demoForm.getFireRoomName().isEmpty()){
			logger.info("Adding fire: " + demoForm.getFireRoomName());
			String[] fires = new String[]{demoForm.getFireRoomName()};
			ruleService.addFire(fires);
		}
		return getIndex(locale, model);
	}
	@Override
	public String remFire(DemoForm demoForm,Locale locale, Model model) {
		if(demoForm.getFireRoomName()!=null && !demoForm.getFireRoomName().isEmpty()){
			logger.info("Removing fire: " + demoForm.getFireRoomName());
			String[] fires = new String[]{demoForm.getFireRoomName()};
			ruleService.remFire(fires);
		}
		return getIndex(locale, model);
	}

	private String getIndex(Locale locale, Model model){
		model.addAttribute("serverTime", DateUtils.getFormattedDate(locale) );
		Collection<Alarm> alarms=ruleService.checkForFire();
		Collection<Sprinkler> sprinklers=ruleService.checkSprinklers();
		Collection<OrderSprinkler> orderSprinklers=ruleService.checkOrderSprinklers();

		Collection<StandardSprinkler> standardSprinklers=ruleService.checkStandardSprinklers();
		model.addAttribute("alarmsFound", alarms!=null && alarms.size()!=0? true:false );
		model.addAttribute("noOfAlarms", alarms.size());
		model.addAttribute("qualifiers", alarms.size());
		model.addAttribute("alarms", alarms);
		model.addAttribute("sprinklers", sprinklers);
		model.addAttribute("orderSprinklers", orderSprinklers);
		model.addAttribute("standardSprinklers", standardSprinklers);
		model.addAttribute("serverTime", DateUtils.getFormattedDate(locale) );
		model.addAttribute("rooms", rooms);
		model.addAttribute("demoForm", new DemoForm());

		return "index";
	}

	@Override
	public String addRule(@ModelAttribute RuleSetupForm ruleSetupForm,Locale locale, Model model) {

		System.out.println("Inside addRule method");
		return null;
	}

	@Override
	public String addStandardRule(DemoForm demoForm, Locale locale, Model model) {
		logger.info("Adding Standard Rule: " + demoForm.getRuleName());
		ruleService.addStandardRule(demoForm);

		return getIndex(locale, model);

	}

	private static void sortListOnBasisOfRule(List<RuleSetup> rulesQualified) {
		Collections.sort(rulesQualified, new Comparator<RuleSetup>(){
			public int compare(RuleSetup o1, RuleSetup o2){
				return o1.getRuleNumber() - o2.getRuleNumber();
			}
		});
	}

	private static Integer getDiscountOnBasisOfQty(Integer quantity, HashMap<Integer, Integer> map) {
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			if (quantity <= entry.getKey()) {
				return entry.getValue();
			}
		}

		return 0;
	}
	
	@Override
	public String deleteOrder(DemoForm demoForm, Locale locale, Model model) {
		Collection<Sprinkler> sprinklers=ruleService.checkSprinklers();
		model.addAttribute("sprinklers", sprinklers);
		ruleService.delOrderSprinklers();
		model.addAttribute("orderSprinklers", null);
		model.addAttribute("noOfAlarms", "Order line deleted");
		
		logger.info("Order line deleted: " + demoForm.getOrderLineNumber());
		
		return ("index");
	}
	
	@Override
	public String deleteRuleSet(DemoForm demoForm, Locale locale, Model model) {
		ruleService.disposeKiSession();

		model.addAttribute("sprinklers", null);
	    model.addAttribute("orderSprinklers", null);
	    model.addAttribute("standardSprinklers", null);
	    
		model.addAttribute("noOfAlarms", "Data reset successful!");
		return ("index");
	}
	

}
