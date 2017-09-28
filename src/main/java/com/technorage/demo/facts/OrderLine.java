package com.technorage.demo.facts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderLine implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer orderLineId;
    
    private Item item;
    
    private Integer quantity;
    
    private Discount discount;
    
    private Account account;
    
    private Product product;
    
    private List<RuleQualifier> ruleQualifier = new ArrayList<RuleQualifier>();
    
    private Integer ruleWinner; 
    
    public OrderLine() {
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void increaseDiscount(double increase) {
        if (discount == null) {
            discount = new Discount(0.0);
        }
        discount.setPercentage(discount.getPercentage() + increase);
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
    
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

	public List<RuleQualifier> getRuleQualifier() {
		return ruleQualifier;
	}

	public void setRuleQualifier(List<RuleQualifier> ruleQualifier) {
		this.ruleQualifier = ruleQualifier;
	}

	public Integer getRuleWinner() {
		return ruleWinner;
	}

	public void setRuleWinner(Integer ruleWinner) {
		this.ruleWinner = ruleWinner;
	}

	public Integer getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Integer orderLineId) {
		this.orderLineId = orderLineId;
	}

	
    
    

}
