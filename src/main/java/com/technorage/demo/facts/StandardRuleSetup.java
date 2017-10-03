package com.technorage.demo.facts;

import java.io.Serializable;
import java.util.HashMap;


/**
 * @author raghav.rampal
 *
 */
public class StandardRuleSetup implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ruleNumber;
	private String ruleName;
	private Account account;
	private Product product;
	private Discount discount;
	private Boolean isQualified = false;
	private Boolean isWinner = false;
	
	private Integer discountRange1;
	private Integer discountRange2;
	private Integer quantityRange1;
	private Integer quantityRange2;
	private Integer quantityRange3;
	private Integer discountRange3;
	private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Integer getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(Integer ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public Boolean getIsQualified() {
		return isQualified;
	}

	public void setIsQualified(Boolean isQualified) {
		this.isQualified = isQualified;
	}

	public Boolean getIsWinner() {
		return isWinner;
	}

	public void setIsWinner(Boolean isWinner) {
		this.isWinner = isWinner;
	}

	public Integer getDiscountRange1() {
		return discountRange1;
	}

	public void setDiscountRange1(Integer discountRange1) {
		this.discountRange1 = discountRange1;
	}

	public Integer getDiscountRange2() {
		return discountRange2;
	}

	public void setDiscountRange2(Integer discountRange2) {
		this.discountRange2 = discountRange2;
	}

	public Integer getQuantityRange1() {
		return quantityRange1;
	}

	public void setQuantityRange1(Integer quantityRange1) {
		this.quantityRange1 = quantityRange1;
	}

	public Integer getQuantityRange2() {
		return quantityRange2;
	}

	public void setQuantityRange2(Integer quantityRange2) {
		this.quantityRange2 = quantityRange2;
	}

	public Integer getQuantityRange3() {
		return quantityRange3;
	}

	public void setQuantityRange3(Integer quantityRange3) {
		this.quantityRange3 = quantityRange3;
	}

	public Integer getDiscountRange3() {
		return discountRange3;
	}

	public void setDiscountRange3(Integer discountRange3) {
		this.discountRange3 = discountRange3;
	}

	public HashMap<Integer, Integer> getMap() {
		return map;
	}

	public void setMap(HashMap<Integer, Integer> map) {
		this.map = map;
	}
	

}
