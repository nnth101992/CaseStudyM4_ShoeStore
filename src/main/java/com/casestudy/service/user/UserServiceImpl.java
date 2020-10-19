package com.casestudy.service.user;

import com.casestudy.exception.UserAlreadyExistException;
import com.casestudy.model.Role;
import com.casestudy.model.User;
import com.casestudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleService roleService;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void remove(Long user_id) {
        userRepository.deleteById(user_id);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

//    @Override
//    public User findByRole(Role role) {
//        return userRepository.findByRole(role);
//    }

    @Override
    public boolean nameExists(String name) {
        return userRepository.findByName(name) != null;
    }

    @Override
    public void registerNewUserAccount(User user) throws UserAlreadyExistException {
        if (nameExists(user.getName()) == true) {
            throw new UserAlreadyExistException("Username was existed!");
        }
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(roleService.findByName("ROLE_USER"));
        newUser.setAvatar(user.getAvatar());
        userRepository.save(newUser);
    }

    @Override
    public void update(User model) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findByName(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(user.getRole());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getName()
        , user.getPassword(), authorities);
        return userDetails;
    }
}
