package com.casestudy.service.product;

import com.casestudy.model.Category;
import com.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<Product> findAll(Pageable pageable);

    Product findByProductId(Long product_id);

    void save(Product Product);

    void remove(Long product_id);

    Page<Product> findAllByCategory(Category category, Pageable pageable);

    Page<Product> findAllByNameContaining(String name, Pageable pageable);


}
