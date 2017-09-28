package com.technorage.demo.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.technorage.demo.facts.OrderSprinkler;

import com.technorage.demo.facts.Alarm;
import com.technorage.demo.facts.RuleSetup;
import com.technorage.demo.facts.Sprinkler;
import com.technorage.demo.facts.Terms;
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
		logger.info("Adding room: " + demoForm.getRoomName());
		ruleService.addRoom(demoForm);
		rooms.put(demoForm.getRoomName(), demoForm.getRoomName());

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
		logger.info("Generating Offer for order line: : " + demoForm.getOrderLineNumber());
		List<String> priorityList = new ArrayList<String>();
		List<RuleSetup> rulesQualified = new ArrayList<RuleSetup>();;
		Collection<RuleSetup> ruleSetupList = ruleService.generateOffer(null);
		
		Integer maxPriority = 0;
		Double totalDiscount = 0.0;
		String winnerRules = "";
		String terms = "";
		String winner = " ";
		String qualifierList = " ";

		for(RuleSetup ruleSetup : ruleSetupList) {
			qualifierList = qualifierList + ", " + ruleSetup.getRuleName();
			priorityList.add(ruleSetup.getWinningPriority());
			rulesQualified.add(ruleSetup);

			if(ruleSetup.getOffer().getPriority() > maxPriority) {
				maxPriority = ruleSetup.getOffer().getPriority();
			}

		}

		sortListOnBasisOfRule(rulesQualified);

		for(RuleSetup setup : rulesQualified) {

			//Display winner with combo field parameters excluded
			if(setup.getOffer().getComboField() == null) {
				if(setup.getWinningPriority().equals("P1")) {
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
				} 
			} 

			//Display winner with combo field parameters included
			else if(setup.getIsWinner()) {
				winner = winner + ", " + setup.getRuleName();
				if(setup.getDiscount()!=null) {
					totalDiscount = totalDiscount + setup.getDiscount().getPercentage();
					winnerRules = winnerRules + ", " + setup.getRuleNumber();
				}

				for(Terms term : setup.getOffer().getTerms()) {
					if(term.getDays() != 0) {
						terms = " and Term " + term.getDays() + " days from Rule " + setup.getRuleNumber();
					}
					if(term.getFreeFreight()) {
						terms = "\n" + terms + "\n having free freight from Rule " + setup.getRuleNumber();
					}
				}
			}
		}


		Collection<Sprinkler> sprinklers = ruleService.checkSprinklers();
		Collection<OrderSprinkler> orderSprinklers=ruleService.checkOrderSprinklers();
		model.addAttribute("sprinklers", sprinklers);
		model.addAttribute("orderSprinklers", orderSprinklers);
		
		model.addAttribute("noOfAlarms", winner.substring(2));
		model.addAttribute("qualifiers", qualifierList.substring(2));
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
		logger.info("Welcome to rules demo! The client locale is {}.", locale);

		model.addAttribute("serverTime", DateUtils.getFormattedDate(locale) );
		Collection<Alarm> alarms=ruleService.checkForFire();
		Collection<Sprinkler> sprinklers=ruleService.checkSprinklers();
		Collection<OrderSprinkler> orderSprinklers=ruleService.checkOrderSprinklers();
		
		model.addAttribute("alarmsFound", alarms!=null && alarms.size()!=0? true:false );
		model.addAttribute("noOfAlarms", alarms.size());
		model.addAttribute("qualifiers", alarms.size());
		model.addAttribute("alarms", alarms);
		model.addAttribute("sprinklers", sprinklers);
		model.addAttribute("orderSprinklers", orderSprinklers);
		model.addAttribute("serverTime", DateUtils.getFormattedDate(locale) );
		model.addAttribute("rooms", rooms);
		model.addAttribute("demoForm", new DemoForm());

		return "index";
	}

	@Override
	public String addRule(@ModelAttribute RuleSetupForm ruleSetupForm,Locale locale, Model model) {

		System.out.println("Hell its Niraj");
		return null;
	}

	private static void sortListOnBasisOfRule(List<RuleSetup> rulesQualified) {
		Collections.sort(rulesQualified, new Comparator<RuleSetup>(){
			public int compare(RuleSetup o1, RuleSetup o2){
				return o1.getRuleNumber() - o2.getRuleNumber();
			}
		});
	}

}
