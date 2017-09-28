package com.technorage.demo.facts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Date date;
    private List<OrderLine> orderLines = new ArrayList<OrderLine>();
    //private Customer customer;
    //private OrderState state;

   /* public Order() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setItems(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public double getTotal() {
        return this.getOrderLines().stream()
                .mapToDouble(item -> item.getItem().getSalePrice() * item.getQuantity())
                .sum();
    }
    
    public int getTotalItems() {
        return this.getOrderLines().stream()
                .mapToInt(item -> item.getQuantity())
                .sum();
    }

    @Override
    public String toString() {
        return "Total " + getOrderLines().size() + " order lines are present.";
    }*/
}
