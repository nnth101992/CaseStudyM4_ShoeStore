package com.casestudy.model;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "Category_SEQ", sequenceName = "SEQUENCE_Category", allocationSize = 1)
    private Long categoryId;

    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;

    public Long getCategoryId() {
        return categoryId;
    }


    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
