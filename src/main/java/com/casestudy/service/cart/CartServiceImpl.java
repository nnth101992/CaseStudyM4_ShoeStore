package com.casestudy.service.cart;

import com.casestudy.model.Cart;
import com.casestudy.model.Product;
import com.casestudy.model.User;
import com.casestudy.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Override
    public Iterable<Cart> findAll() {

        return cartRepository.findAll();
    }

    @Override
    public Cart findByCartId(Long cart_id) {
        return cartRepository.findById(cart_id).orElse(null);
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void remove(Long cart_id) {
        cartRepository.deleteById(cart_id);
    }

    @Override
    public Iterable<Cart> findAllByUser(User user) {
        return cartRepository.findAllByUser(user);
    }

    @Override
    public Iterable<Cart> findAllByOrderNumberAndUser(Long orderNumber, User user) {
        return cartRepository.findAllByOrderNumberAndUser(orderNumber, user);
    }

    @Override
    public Cart findByProductAndUserAndOrderNumber(Product product, User user, Long orderNumber) {
        return cartRepository.findByProductAndUserAndOrderNumber(product, user, orderNumber);
    }

    @Override
    public Long countByOrderNumberAndUser(Long orderNumber, User user) {
        return Long.valueOf(((Collection<?>) this.findAllByOrderNumberAndUser(orderNumber, user)).size());
    }
}
