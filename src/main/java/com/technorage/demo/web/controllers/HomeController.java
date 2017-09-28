package com.technorage.demo.web.controllers;

import java.util.Locale;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.technorage.demo.forms.DemoForm;
import com.technorage.demo.forms.RuleSetupForm;


public interface HomeController {
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index(Locale locale, Model model);
    
    @RequestMapping(value="/addroom", method=RequestMethod.POST)
    public String addRoom(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/addOrder", method=RequestMethod.POST)
    public String addOrder(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/addfire", method=RequestMethod.POST)
    public String addFire(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/remfire", method=RequestMethod.POST)
    public String remFire(@ModelAttribute DemoForm demoForm,Locale locale, Model model);
    
    @RequestMapping(value="/addRule", method=RequestMethod.POST)
    public String addRule(@ModelAttribute RuleSetupForm ruleSetupForm,Locale locale, Model model);
    
}
