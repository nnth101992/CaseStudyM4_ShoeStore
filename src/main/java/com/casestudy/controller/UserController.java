package com.casestudy.controller;

import com.casestudy.exception.NotFoundException;
import com.casestudy.exception.UserAlreadyExistException;
import com.casestudy.model.User;
import com.casestudy.service.product.ProductService;
import com.casestudy.service.user.RoleService;
import com.casestudy.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/home")
public class UserController {

    @Autowired
    ProductService productServiceImpl;

    @Autowired
    UserService userServiceImpl;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleService roleServiceImpl;

    private String getPrincipal() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }


    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView(("login"));
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/home/login";
    }

    @GetMapping()
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("username", getPrincipal());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView formCreate() {
        ModelAndView modelAndView = new ModelAndView("signUp");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveUser(@ModelAttribute("user") @Valid User user) {
        try {
            userServiceImpl.registerNewUserAccount(user);
        } catch (UserAlreadyExistException e) {
            ModelAndView modelAndView = new ModelAndView("signUp");
            modelAndView.addObject("message", "Account Existed");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("signUp");
        modelAndView.addObject("message1", "Successfully Registration");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView formEdit() {
        ModelAndView modelAndView = new ModelAndView("editUser");
        modelAndView.addObject("user", userServiceImpl.findByName(getPrincipal()));
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView saveEdit(@ModelAttribute("user") @Valid User user) {
        String pwEn = passwordEncoder.encode(user.getPassword());
        user.setPassword(pwEn);
        user.setRole(roleServiceImpl.findByName("ROLE_USER"));
        userServiceImpl.save(user);
        ModelAndView modelAndView = new ModelAndView("editUser");
        modelAndView.addObject("message", "Update password successfully");
        return modelAndView;
    }

    @GetMapping("/listUser")
    public ModelAndView listUser() {
        Iterable<User> users = userServiceImpl.findAll();
        ModelAndView modelAndView = new ModelAndView("listUser");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/listUser/delete/{userId}")
    public ModelAndView showDeleteForm(@PathVariable Long userId) throws NotFoundException {
        User user = userServiceImpl.findById(userId);
        ModelAndView modelAndView;
        if (user != null) {
            modelAndView = new ModelAndView("deleteUser");
            modelAndView.addObject("user", user);
        } else {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @PostMapping("/listUser/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userServiceImpl.remove(user.getUserId());
        return "redirect:/home/listUser";
    }





}
