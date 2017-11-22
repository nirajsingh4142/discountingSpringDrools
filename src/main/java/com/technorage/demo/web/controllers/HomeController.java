package com.technorage.demo.web.controllers;

import java.util.Locale;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.technorage.demo.forms.DemoForm;


public interface HomeController {
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index(Locale locale, Model model);
    
    @RequestMapping(value="/addRule", method=RequestMethod.POST)
    public String addRule(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/addOrder", method=RequestMethod.POST)
    public String addOrder(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/generateOffer", method=RequestMethod.POST)
    public String generateOffer(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/addStandardRule", method=RequestMethod.POST)
	String addStandardRule(DemoForm demoForm, Locale locale, Model model);

    @RequestMapping(value="/deleteOrder", method=RequestMethod.POST)
    public String deleteOrder(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/deleteRuleSet", method=RequestMethod.POST)
    public String deleteRuleSet(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/exportRuleData", method=RequestMethod.POST)
    public String exportRuleData(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
}
