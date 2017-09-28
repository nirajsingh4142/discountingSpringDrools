package com.technorage.demo.facts;

import java.io.Serializable;

/**
 * @author raghav.rampal
 *
 */
public class RuleSetup implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ruleNumber;
	private String ruleName;
	private Account account;
	private Product product;
	private Discount discount;
	private Offer offer;
	private Boolean isQualified = false;
	private Boolean isWinner = false;
	private String winningPriority = "";

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

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
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

	public String getWinningPriority() {
		return winningPriority;
	}

	public void setWinningPriority(String winningPriority) {
		this.winningPriority = winningPriority;
	}

	public Boolean getIsWinner() {
		return isWinner;
	}

	public void setIsWinner(Boolean isWinner) {
		this.isWinner = isWinner;
	}


}
