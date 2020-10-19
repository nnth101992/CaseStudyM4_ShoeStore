package com.casestudy.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
//    @NotEmpty
//    @NotNull
//    @NotBlank
//    @Size(min = 3, max = 12)
    private String name;

    @Column(nullable = false)
//    @NotEmpty
//    @NotNull
//    @NotBlank
//    @Size(min = 6, max = 255)
    private String password;


    @ManyToOne
    public Role role;

    private String avatar;

    @OneToMany(mappedBy = "user")
    private Set<Cart> carts;

    private Long orderNumber;

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public User() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    public User(Long userId, String name, String password, Role role, String avatar, Set<Cart> carts) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
        this.carts = carts;
    }

    public User(String name, String password, Role role, String avatar, Set<Cart> carts) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
        this.carts = carts;
    }
}
