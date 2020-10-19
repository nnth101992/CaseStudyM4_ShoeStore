package com.casestudy.service.orderprice;


import com.casestudy.model.OrderDetails;
import com.casestudy.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Override
    public Iterable<OrderDetails> findAll() {
        return orderDetailsRepository.findAll();
    }

    @Override
    public OrderDetails findByOrderId(Long orderId) {
        return orderDetailsRepository.findById(orderId).orElse(null);
    }

    @Override
    public void save(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }

    @Override
    public void remove(Long orderId) {
        orderDetailsRepository.deleteById(orderId);
    }

    @Override
    public OrderDetails findByOrderNumber(Long orderNumber) {
        return orderDetailsRepository.findByOrderNumber(orderNumber);
    }
}
