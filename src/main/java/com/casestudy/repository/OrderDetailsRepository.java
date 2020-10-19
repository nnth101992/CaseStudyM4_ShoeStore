package com.casestudy.repository;

import com.casestudy.model.OrderDetails;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {

    OrderDetails findByOrderNumber(Long orderNumber);
}
