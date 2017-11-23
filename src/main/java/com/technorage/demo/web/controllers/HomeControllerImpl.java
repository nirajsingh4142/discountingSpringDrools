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

import com.technorage.demo.facts.Alarm;
import com.technorage.demo.facts.OrderSprinkler;
import com.technorage.demo.facts.RuleSetup;
import com.technorage.demo.facts.Sprinkler;
import com.technorage.demo.facts.StandardRuleSetup;
import com.technorage.demo.facts.StandardSprinkler;
import com.technorage.demo.forms.DemoForm;
import com.technorage.demo.services.DemoRuleService;
import com.technorange.demo.utility.Constants;
import com.technorange.demo.utility.ExportRuleSetupData;

@Controller
public class HomeControllerImpl implements HomeController {

	private static Logger logger = LoggerFactory.getLogger(HomeControllerImpl.class);
	private Map<String, String> rooms = new LinkedHashMap<String,String>();

	@Autowired
	private DemoRuleService<?> ruleService;

	@Override
	public String index(Locale locale, Model model) {
		return getIndex(locale, model);
	}

	@Override
	public String addRule(DemoForm demoForm, Locale locale, Model model) {
		logger.info("Adding rule: " + demoForm.getRuleName());
		ruleService.addRule(demoForm);
		rooms.put(demoForm.getRuleName(), demoForm.getRuleName());

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
		List<RuleSetup> tempRulesQualified = new ArrayList<RuleSetup>();

		Collection<StandardRuleSetup> standardRulesQualified = ruleService.getStandardRulesQualified(null);
		Collection<RuleSetup> rulesQualifiedList = ruleService.generateOffer(null);

		Double netDiscount = 0.0;
		String displayWinnerRule = "";
		String displayWinnerTerms = "";
		String resultString = " ";
		String displayQualifierRule = " ";
		int tempDiscount = 0;

		//fetch order line quantity; only one order line will be processed in single time
		int usedQuantity = 0;
		for(OrderSprinkler lines : ruleService.getOrderLines()) {
			if(lines.getOrderLine().getQuantity()!=null) {
				usedQuantity = lines.getOrderLine().getQuantity();	
			}

		}

		//for case rule is qualified
		for(RuleSetup ruleSetup : rulesQualifiedList) {
			displayQualifierRule = displayQualifierRule + ", " + ruleSetup.getRuleName();
			tempRulesQualified.add(ruleSetup);

			tempDiscount = getDiscountOnBasisOfQty(usedQuantity, ruleSetup.getMap());
			if(tempDiscount == 0) {

				//case no standard rule exists
				if(ruleSetup.getDiscount()!=null && ruleSetup.getDiscount().getPercentage()!=null) {
					tempDiscount = ruleSetup.getDiscount().getPercentage().intValue();
				}

				//take discount from standard rule if exists
				for(StandardRuleSetup stdRule : standardRulesQualified) {
					tempDiscount = getDiscountOnBasisOfQty(usedQuantity, stdRule.getMap());
				}

			}

			resultString = "  " + ruleSetup.getRuleName() + " wins with discount: " + tempDiscount + "%";
		}

		sortListOnBasisOfRule(tempRulesQualified);

		// standard rule will execute only when none of account rules is qualified
		if(tempRulesQualified.isEmpty()) {
			for(StandardRuleSetup setup : standardRulesQualified) {
				displayQualifierRule = displayQualifierRule + ", " + setup.getRuleName();
				tempDiscount = getDiscountOnBasisOfQty(usedQuantity, setup.getMap());
				resultString = "  " +  setup.getRuleName() + " wins with discount: " + tempDiscount + "%";
			}
		}

		for(RuleSetup setup : tempRulesQualified) {
			//Display winner with combo field parameters included
			if(setup.getIsWinner()) {
				if(setup.getDiscount()!=null && setup.getDiscount().getPercentage()!=null) {
					resultString = resultString + ", " + setup.getRuleName();
					netDiscount = netDiscount + setup.getDiscount().getPercentage();
					displayWinnerRule = displayWinnerRule + ", " + setup.getRuleNumber();
				}

				if(setup.getOffer().getDays() != null) {
					displayWinnerTerms = " and Term " + setup.getOffer().getDays() + " days from Rule " + setup.getRuleNumber();
				}
				//if freight charge is false, it is free freight 
				if(setup.getOffer().getFrieghtCharge().equalsIgnoreCase(Constants.BOOLEAN_FALSE)) {
					displayWinnerTerms = "\n" + displayWinnerTerms + "\n having free freight from Rule " + setup.getRuleNumber();
				}
			}

		}

		if(!displayWinnerRule.isEmpty()) {
			resultString = "  Rule " + displayWinnerRule.substring(2) + " wins with discount: " + netDiscount + "%" + displayWinnerTerms;
		}

		Collection<Sprinkler> sprinklers = ruleService.checkSprinklers();
		Collection<StandardSprinkler> standardSprinklers=ruleService.checkStandardSprinklers();

		model.addAttribute("sprinklers", sprinklers);
		model.addAttribute("orderSprinklers", ruleService.getOrderLines());
		model.addAttribute("standardSprinklers", standardSprinklers);

		if(!resultString.equals(" ")) {
			model.addAttribute(Constants.NET_OUTPUT, resultString.substring(2));
		}
		if(!displayQualifierRule.equals(" ")) {
			model.addAttribute("qualifiers", displayQualifierRule.substring(2));
		}

		return ("index");
	}

	private String getIndex(Locale locale, Model model){
		Collection<Alarm> alarms=ruleService.checkForFire();
		Collection<Sprinkler> sprinklers=ruleService.checkSprinklers();
		Collection<OrderSprinkler> orderSprinklers=ruleService.getOrderLines();

		Collection<StandardSprinkler> standardSprinklers=ruleService.checkStandardSprinklers();
		model.addAttribute("alarmsFound", alarms!=null && alarms.size()!=0? true:false );
		model.addAttribute(Constants.NET_OUTPUT, alarms.size());
		model.addAttribute("qualifiers", alarms.size());
		model.addAttribute("alarms", alarms);
		model.addAttribute("sprinklers", sprinklers);
		model.addAttribute("orderSprinklers", orderSprinklers);
		model.addAttribute("standardSprinklers", standardSprinklers);
		model.addAttribute("rooms", rooms);
		model.addAttribute("demoForm", new DemoForm());

		return "index";
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
		model.addAttribute(Constants.NET_OUTPUT, "Order line deleted");

		logger.info("Order line deleted: " + demoForm.getOrderLineNumber());

		return ("index");
	}

	@Override
	public String deleteRuleSet(DemoForm demoForm, Locale locale, Model model) {
		ruleService.disposeKiSession();

		model.addAttribute("sprinklers", null);
		model.addAttribute("orderSprinklers", null);
		model.addAttribute("standardSprinklers", null);

		model.addAttribute(Constants.NET_OUTPUT, "Data reset successful!");
		return ("index");
	}

	@Override
	public String exportRuleData(Model model) {
		ExportRuleSetupData.generateRuleSetupRows(ruleService);
		
		model.addAttribute(Constants.NET_OUTPUT, "RuleDataExport.xlsx created successfully!");
		return ("index");
	}

	
}
