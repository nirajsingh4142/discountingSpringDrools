package com.technorage.demo.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.technorage.demo.drools.FactFinder;
import com.technorage.demo.drools.monitoring.TrackingAgendaEventListener;
import com.technorage.demo.drools.monitoring.TrackingWorkingMemoryEventListener;
import com.technorage.demo.drools.spring.DefaultKieSessionBean;
import com.technorage.demo.drools.spring.KieContainerBean;
import com.technorage.demo.drools.spring.KieServicesBean;
import com.technorage.demo.drools.spring.KieSessionBean;
import com.technorage.demo.facts.Account;
import com.technorage.demo.facts.Alarm;
import com.technorage.demo.facts.Discount;
import com.technorage.demo.facts.Fire;
import com.technorage.demo.facts.Offer;
import com.technorage.demo.facts.OrderLine;
import com.technorage.demo.facts.OrderSprinkler;
import com.technorage.demo.facts.Product;
import com.technorage.demo.facts.Room;
import com.technorage.demo.facts.RulePrecedence;
import com.technorage.demo.facts.RuleSetup;
import com.technorage.demo.facts.Sprinkler;
import com.technorage.demo.facts.StandardRuleSetup;
import com.technorage.demo.facts.StandardSprinkler;
import com.technorage.demo.forms.DemoForm;

@Service
@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode=ScopedProxyMode.INTERFACES)
public class DemoRuleServiceImpl<T> implements DemoRuleService<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4279066046268640811L;

	private static Logger log = LoggerFactory.getLogger(DemoRuleServiceImpl.class);

	public KieSessionBean kieSession;

	private TrackingAgendaEventListener agendaEventListener;
	private TrackingWorkingMemoryEventListener workingMemoryEventListener;

	private Map<String,Room> name2room = new HashMap<String,Room>();
	private Map<String,FactHandle> fact2fire = new HashMap<String,FactHandle>();
	private FactFinder<OrderSprinkler> findOrderSprinklers=new FactFinder<>(OrderSprinkler.class);
	private FactFinder<StandardSprinkler> findStandardSprinkler=new FactFinder<>(StandardSprinkler.class);
	
	private FactFinder<Alarm> findAlarms=new FactFinder<>(Alarm.class);
	private FactFinder<Sprinkler> findSprinklers=new FactFinder<>(Sprinkler.class);
	private FactFinder<RuleSetup> findRuleSetups = new FactFinder<>(RuleSetup.class);
	private FactFinder<StandardRuleSetup> findStandardRuleSetups = new FactFinder<>(StandardRuleSetup.class);

	@Autowired
	public DemoRuleServiceImpl(
			@Qualifier("demoKieContainer") KieContainerBean kieContainer,@Qualifier("demoKieServices") KieServicesBean kieServices) {

		kieSession = new DefaultKieSessionBean(kieServices, kieContainer);

		agendaEventListener = new TrackingAgendaEventListener();
		workingMemoryEventListener = new TrackingWorkingMemoryEventListener();

		kieSession.addEventListener(agendaEventListener);
		kieSession.addEventListener(workingMemoryEventListener);

	}

	@Override
	public Collection<Alarm> addFire(String[]  fires) {

		for( String fire: fires ){

			if(!fact2fire.containsKey(fire)){
				Fire roomFire = new Fire( name2room.get(fire) );
				FactHandle roomFireHandle = kieSession.insert( roomFire );
				fact2fire.put( fire, roomFireHandle );
			}

		}

		kieSession.fireAllRules();
		//  Collection<T> result = findFact.findFacts(kieSession, new BeanPropertyFilter("key", ""));     

		Collection<Alarm> result = findAlarms.findFacts(kieSession);

		return result;
	}

	@Override
	public Collection<Alarm> remFire(String[]  fires) {

		for( String fire: fires ){
			if(fact2fire.containsKey(fire)){
				kieSession.delete( fact2fire.get(fire) );
				fact2fire.remove( fire );
			}
		}


		kieSession.fireAllRules();

		Collection<Alarm> result = findAlarms.findFacts(kieSession);

		return result;
	}

	@Override
	public void addRoom(DemoForm demoForm) {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		RuleSetup ruleSetup = new RuleSetup();
		ruleSetup.setRuleNumber(demoForm.getRuleNumber());
		ruleSetup.setRuleName(demoForm.getRuleName());
		Account account = new Account();
		account.setAccountNumber(demoForm.getAccountNumber());
		if(demoForm.getAccountType().equals("")) {
			account.setAccountType(null);
		}else {
			account.setAccountType(demoForm.getAccountType());
		}
		ruleSetup.setAccount(account);

		Product product = new Product();
		if(demoForm.getFc().equals("")) {
			product.setFamilyCode(null);
		}else {
			product.setFamilyCode(demoForm.getFc());
		}
		product.setIsbn(demoForm.getIsbn());
		if(demoForm.getDgp().equals("")) {
			product.setProductGroupCode(null);
		}else {
			product.setProductGroupCode(demoForm.getDgp());
		}
		ruleSetup.setProduct(product);

		Discount discount = new Discount();
		discount.setPercentage(demoForm.getDiscount());
		ruleSetup.setDiscount(discount);

		Offer offer =new Offer();
		offer.setComboField(demoForm.getCombo());
		offer.setHardcode(demoForm.isHardcode());
		offer.setPriority(demoForm.getPriority());
		offer.setOverridenExplicitly(demoForm.isOverridenExplicitly());

		offer.setDays(demoForm.getTerms());
		offer.setFrieghtCharge(demoForm.getFrieghtCharge());

		ruleSetup.setOffer(offer);
		
		ruleSetup.setDiscountRange1(demoForm.getDiscountRange1());
		ruleSetup.setDiscountRange2(demoForm.getDiscountRange2());
		ruleSetup.setQuantityRange1(demoForm.getQuantityRange1());
		ruleSetup.setQuantityRange2(demoForm.getQuantityRange2());
		
		if(demoForm.getQuantityRange1()!=null && demoForm.getDiscountRange1()!=null) {
			map.put(demoForm.getQuantityRange1(), demoForm.getDiscountRange1());
		}
		if(demoForm.getQuantityRange2()!=null && demoForm.getDiscountRange2()!=null) {
			map.put(demoForm.getQuantityRange2(), demoForm.getDiscountRange2());
		}
		
		ruleSetup.setMap(map);
		
		kieSession.insert(ruleSetup );
		kieSession.insert(ruleSetup.getOffer());

		Sprinkler sprinkler = new Sprinkler( ruleSetup );
		kieSession.insert( sprinkler );

	}
	
	@Override
	public void addStandardRule(DemoForm demoForm) {
		HashMap<Integer, Integer> map = new HashMap<>();
		StandardRuleSetup standardRuleSetup = new StandardRuleSetup();
		standardRuleSetup.setRuleNumber(demoForm.getRuleNumber());
		standardRuleSetup.setRuleName(demoForm.getRuleName());
		
		Account account = new Account();
		if(demoForm.getAccountType().equals("")) {
			account.setAccountType(null);
		}else {
			account.setAccountType(demoForm.getAccountType());
		}
		standardRuleSetup.setAccount(account);

		Product product = new Product();
		if(demoForm.getFc().equals("")) {
			product.setFamilyCode(null);
		}else {
			product.setFamilyCode(demoForm.getFc());
		}
		product.setIsbn(demoForm.getIsbn());
		standardRuleSetup.setProduct(product);

		standardRuleSetup.setDiscountRange1(demoForm.getDiscountRange1());
		standardRuleSetup.setDiscountRange2(demoForm.getDiscountRange2());
		standardRuleSetup.setQuantityRange1(demoForm.getQuantityRange1());
		standardRuleSetup.setQuantityRange2(demoForm.getQuantityRange2());
		standardRuleSetup.setDiscountRange3(demoForm.getDiscountRange3());
		standardRuleSetup.setQuantityRange3(demoForm.getQuantityRange3());
		
		if(demoForm.getQuantityRange1()!=null && demoForm.getDiscountRange1()!=null) {
			map.put(demoForm.getQuantityRange1(), demoForm.getDiscountRange1());
		}
		if(demoForm.getQuantityRange2()!=null && demoForm.getDiscountRange2()!=null) {
			map.put(demoForm.getQuantityRange2(), demoForm.getDiscountRange2());
		}
		if(demoForm.getQuantityRange3()!=null && demoForm.getDiscountRange3()!=null) {
			map.put(demoForm.getQuantityRange3(), demoForm.getDiscountRange3());
		}
		
		standardRuleSetup.setMap(map);
		
		Offer offer =new Offer();
		offer.setComboField(demoForm.getCombo());
		offer.setHardcode(demoForm.isHardcode());
		offer.setPriority(demoForm.getPriority());
		offer.setOverridenExplicitly(demoForm.isOverridenExplicitly());

		offer.setDays(demoForm.getTerms());
		offer.setFrieghtCharge(demoForm.getFrieghtCharge());

		kieSession.insert(standardRuleSetup );

		StandardSprinkler sprinkler = new StandardSprinkler( standardRuleSetup );
		kieSession.insert( sprinkler );

	}

	@Override
	public Collection<Alarm> checkForFire() {

		Collection<Alarm> result = findAlarms.findFacts(kieSession);

		return result;
	}

	@Override
	public Collection<Sprinkler> checkSprinklers() {

		Collection<Sprinkler> result = findSprinklers.findFacts(kieSession);

		return result;
	}

	@Override
	public Room getRoom(String name) {

		return name2room.get(name);
	}

	@Override
	public void addOrder(DemoForm demoForm) {

		OrderLine orderLine = new OrderLine();
		orderLine.setOrderLineId(demoForm.getOrderLineNumber());
		Account account = new Account();
		account.setAccountNumber(demoForm.getAccountNumber());
		account.setAccountType(demoForm.getAccountType());
		orderLine.setAccount(account);

		Product product = new Product();
		product.setFamilyCode(demoForm.getFc());
		product.setIsbn(demoForm.getIsbn());
		product.setProductGroupCode(demoForm.getDgp());
		orderLine.setProduct(product);
		orderLine.setQuantity(demoForm.getQuantity());

		kieSession.insert( orderLine );
		OrderSprinkler orderSprinkler = new OrderSprinkler( orderLine );
		kieSession.insert( orderSprinkler );

	}

	@Override	
	public Collection<RuleSetup> generateOffer(DemoForm demoForm) {
		loadPropFile();
		
		System.out.println("Bootstrapping the Rule Engine ..." );
		kieSession.fireAllRules();
		System.out.println("Rules fired: " + kieSession.fireAllRules());
		
		Collection<RuleSetup> result = findRuleSetups.findFacts(kieSession);
		List<RuleSetup> finalRule = new ArrayList<RuleSetup>();
		
		for(RuleSetup rule : result) {
			if(rule.getIsQualified()) {
				finalRule.add(rule);
				System.out.println("Rules qualified: " + rule.getRuleName());
			}
		}
		
		System.out.println("Bootstrapping the Rule Engine ..." );
		
		return finalRule;
	}
	
	public Collection<StandardRuleSetup> getStandardRulesQualified() {
		Collection<StandardRuleSetup> standardRuleResult = findStandardRuleSetups.findFacts(kieSession);
		List<StandardRuleSetup> standardRulesQualified = new ArrayList<StandardRuleSetup>();
		
		for(StandardRuleSetup rule : standardRuleResult) {
			if(rule.getIsQualified()) {
				standardRulesQualified.add(rule);
				
				System.out.println("Standard Rules qualified: " + rule.getRuleName());
			}
		}
		
		return standardRulesQualified;
	}
	
	@Override
	public Collection<OrderSprinkler> checkOrderSprinklers() {

		Collection<OrderSprinkler> result = findOrderSprinklers.findFacts(kieSession);

		return result;
	}
	
	private void loadPropFile() {
		//load dynamic prop file
		List<RulePrecedence> rulePrecedences = RulePrecedenceBuilder.loadRulePrecedence();
		for(RulePrecedence precedences : rulePrecedences) {
			kieSession.insert(precedences);
		}
	}

	@Override
	public Collection<StandardSprinkler> checkStandardSprinklers() {
		Collection<StandardSprinkler> result = findStandardSprinkler.findFacts(kieSession);
		
		return result;
	}

	@Override	
	public Collection<StandardRuleSetup> getStandardRulesQualified(DemoForm demoForm) {
		loadPropFile();
		
		System.out.println("Bootstrapping the Rule Engine ..." );
		kieSession.fireAllRules();
		System.out.println("Rules fired: " + kieSession.fireAllRules());
		
		Collection<StandardRuleSetup> finalRule = findStandardRuleSetups.findFacts(kieSession);
		
		for(StandardRuleSetup rule : finalRule) {
			if(rule.getIsQualified()) {
				finalRule.add(rule);
				System.out.println("Rules qualified: " + rule.getRuleName());
			}
		}
		
		System.out.println("Bootstrapping the Rule Engine ..." );
		
		return finalRule;
	}

}
