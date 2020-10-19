package com.casestudy.repository;

import com.casestudy.model.Cart;
import com.casestudy.model.Category;
import com.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
//    Iterable<Product> findAllByProduct(Product product);

    Page<Product> findAllByNameContaining(String name , Pageable pageable);

    Page<Product> findAllByCategory(Category category, Pageable pageable);


}
