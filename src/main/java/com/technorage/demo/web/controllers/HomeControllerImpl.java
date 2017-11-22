package com.technorage.demo.web.controllers;

import java.io.File;
import java.io.FileOutputStream;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
				if(setup.getOffer().getFrieghtCharge().equalsIgnoreCase("false")) {
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
			model.addAttribute("netOutput", resultString.substring(2));
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
		model.addAttribute("netOutput", alarms.size());
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
		model.addAttribute("netOutput", "Order line deleted");

		logger.info("Order line deleted: " + demoForm.getOrderLineNumber());

		return ("index");
	}

	@Override
	public String deleteRuleSet(DemoForm demoForm, Locale locale, Model model) {
		ruleService.disposeKiSession();

		model.addAttribute("sprinklers", null);
		model.addAttribute("orderSprinklers", null);
		model.addAttribute("standardSprinklers", null);

		model.addAttribute("netOutput", "Data reset successful!");
		return ("index");
	}

	@Override
	public String exportRuleData(DemoForm demoForm, Locale locale, Model model) {
		Collection<Sprinkler> sprinklerList = ruleService.checkSprinklers();
		try	{    
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("Rule Number");
			header.createCell(1).setCellValue("Rule Name");

			header.createCell(2).setCellValue("Account Number");
			header.createCell(3).setCellValue("Account Type");

			header.createCell(4).setCellValue("ISBN");
			header.createCell(5).setCellValue("Family Code");
			header.createCell(6).setCellValue("DGP");

			header.createCell(7).setCellValue("Quantity Range-1");
			header.createCell(8).setCellValue("Discount Range-1");
			header.createCell(9).setCellValue("Quantity Range-2");
			header.createCell(10).setCellValue("Discount Range-2");
			header.createCell(11).setCellValue("Freight Charge");
			header.createCell(12).setCellValue("Override Explicitly");
			header.createCell(13).setCellValue("Hardcode");
			header.createCell(14).setCellValue("Terms");
			header.createCell(15).setCellValue("Combo Field");

			header.createCell(16).setCellValue("Priority");
			header.createCell(17).setCellValue("Discount");

			int rowCount = 0;
			Row row = null;
			for (Sprinkler sprinkler : sprinklerList) {
				row = sheet.createRow( ++rowCount);
				createCell(sprinkler, row);
			}

			FileOutputStream out = new FileOutputStream(new File("C:/RuleDataExport.xlsx"));
			workbook.write(out);
			out.close();
		} 
		catch (Exception e)	{
			e.printStackTrace();
		}

		model.addAttribute("netOutput", "RuleDataExport.xlsx created successfully!");
		return ("index");
	}

	private void createCell(Sprinkler sprinkler, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(sprinkler.getRuleSetup().getRuleNumber());

		cell = row.createCell(1);
		cell.setCellValue(sprinkler.getRuleSetup().getRuleName());

		cell = row.createCell(2);
		if(sprinkler.getRuleSetup().getAccount().getAccountNumber()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getAccount().getAccountNumber());
		}

		cell = row.createCell(3);
		cell.setCellValue(sprinkler.getRuleSetup().getAccount().getAccountType());

		cell = row.createCell(4);
		if(sprinkler.getRuleSetup().getProduct().getIsbn()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getProduct().getIsbn());
		}

		cell = row.createCell(5);
		cell.setCellValue(sprinkler.getRuleSetup().getProduct().getFamilyCode());

		cell = row.createCell(6);
		cell.setCellValue(sprinkler.getRuleSetup().getProduct().getProductGroupCode());

		cell = row.createCell(7);
		if(sprinkler.getRuleSetup().getQuantityRange1()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getQuantityRange1());
		}

		cell = row.createCell(8);
		if(sprinkler.getRuleSetup().getDiscountRange1()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getDiscountRange1());
		}

		cell = row.createCell(9);
		if(sprinkler.getRuleSetup().getQuantityRange2()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getQuantityRange2());
		}

		cell = row.createCell(10);
		if(sprinkler.getRuleSetup().getDiscountRange2()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getDiscountRange2());
		}

		cell = row.createCell(11);
		cell.setCellValue(sprinkler.getRuleSetup().getOffer().getFrieghtCharge());

		cell = row.createCell(12);
		cell.setCellValue(sprinkler.getRuleSetup().getOffer().getOverridenExplicitly());

		cell = row.createCell(13);
		cell.setCellValue(sprinkler.getRuleSetup().getOffer().getHardcode());

		cell = row.createCell(14);
		if(sprinkler.getRuleSetup().getOffer().getDays()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getOffer().getDays());
		}

		cell = row.createCell(15);
		if(sprinkler.getRuleSetup().getOffer().getComboField()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getOffer().getComboField());
		}

		cell = row.createCell(16);
		if(sprinkler.getRuleSetup().getOffer().getPriority()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getOffer().getPriority());
		}

		cell = row.createCell(17);
		if(sprinkler.getRuleSetup().getDiscount().getPercentage()!=null) {
			cell.setCellValue(sprinkler.getRuleSetup().getDiscount().getPercentage());
		}

	}

}
