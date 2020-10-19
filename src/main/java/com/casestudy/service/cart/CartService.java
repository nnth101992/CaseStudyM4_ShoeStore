package com.casestudy.service.cart;

import com.casestudy.model.Cart;
import com.casestudy.model.Product;
import com.casestudy.model.User;

import java.util.Optional;


public interface CartService {

    Iterable<Cart> findAll();

    Cart findByCartId(Long cart_id);

    void save(Cart cart);

    void remove(Long cart_id);

    Iterable<Cart> findAllByUser(User user);

    Iterable<Cart> findAllByOrderNumberAndUser(Long orderNumber, User user);

    Cart findByProductAndUserAndOrderNumber(Product product, User user, Long orderNumber);

    Long countByOrderNumberAndUser(Long orderNumber, User user);
}
