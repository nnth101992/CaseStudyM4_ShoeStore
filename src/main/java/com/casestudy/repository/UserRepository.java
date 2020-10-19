package com.casestudy.repository;

import com.casestudy.model.Role;
import com.casestudy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByName(String username);

    Iterable<User> findAllByRole(Role role);

//    User findByRole(Role role);
}