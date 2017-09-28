package com.technorage.demo.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.technorage.demo.facts.RulePrecedence;

public class RulePrecedenceBuilder {
	public static List<RulePrecedence> loadRulePrecedence() {

		List<RulePrecedence> listRulePrecedence = new ArrayList<RulePrecedence>();
		RulePrecedence rulePrecedence;
		try {
			File file = new File("D:\\HBG_OTC_Discounting\\discountingSpringDrools\\src\\main\\java\\com\\technorage\\demo\\utils\\priorityConfig.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				rulePrecedence = new RulePrecedence();	

				String key = (String) enuKeys.nextElement();
				rulePrecedence.setRuleName(key);
				
				String value = properties.getProperty(key);
				rulePrecedence.setPriority(value);

				listRulePrecedence.add(rulePrecedence);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listRulePrecedence;
	}

}
