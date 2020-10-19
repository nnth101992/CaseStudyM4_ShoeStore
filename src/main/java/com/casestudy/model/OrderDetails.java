package com.casestudy.model;

import javax.persistence.*;

@Entity
@Table(name = "orderdetails")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long orderNumber;

    private Long totalPrice;

    public Long getOrderPriceId() {
        return orderId;
    }

    public void setOrderPriceId(Long orderPriceId) {
        this.orderId = orderPriceId;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
