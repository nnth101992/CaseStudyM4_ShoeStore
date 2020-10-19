package com.casestudy.service.user;

import com.casestudy.exception.UserAlreadyExistException;
import com.casestudy.model.Role;
import com.casestudy.model.User;
import com.casestudy.service.Service;

public interface UserService extends Service<User> {

    User findByName(String name);
    boolean nameExists(String email);
    void registerNewUserAccount(User user)
            throws UserAlreadyExistException;
    public void update(User model);
//    User findByRole(Role role);
}
