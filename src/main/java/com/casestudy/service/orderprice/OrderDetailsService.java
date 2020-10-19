package com.casestudy.service.orderprice;


import com.casestudy.model.OrderDetails;

public interface OrderDetailsService {

    Iterable<OrderDetails> findAll();

    OrderDetails findByOrderId(Long orderId);

    void save(OrderDetails orderDetails);

    void remove(Long orderId);

    OrderDetails findByOrderNumber(Long orderNumber);
}
