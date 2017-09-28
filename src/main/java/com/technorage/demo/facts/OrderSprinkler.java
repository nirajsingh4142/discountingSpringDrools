package com.technorage.demo.facts;
public class OrderSprinkler {

   
    private OrderLine orderLine;

    private boolean on;

	public OrderLine getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(OrderLine orderLine) {
		this.orderLine = orderLine;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public OrderSprinkler(OrderLine orderLine) {
		super();
		this.orderLine = orderLine;
	}
	

 
}