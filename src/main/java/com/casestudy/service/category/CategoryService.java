package com.casestudy.service.category;

import com.casestudy.model.Category;

import java.util.Optional;


public interface CategoryService {
    Iterable<Category> findAll();

    Category findByCategoryId(Long categoryId);

    void save(Category category);

    void remove(Long categoryId);


}
