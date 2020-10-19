package com.casestudy.controller;

import com.casestudy.model.*;
import com.casestudy.service.cart.CartService;
import com.casestudy.service.category.CategoryService;
import com.casestudy.service.orderprice.OrderDetailsService;
import com.casestudy.service.product.ProductService;
import com.casestudy.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductController productController;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @ModelAttribute("username")
    public String getPrincipal() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    @ModelAttribute("role")
    public String getRole() {
        String name = this.getPrincipal();
        if (name.contains("anonymousUser") ) {
            return null;
        } else {
            return userService.findByName(name).getRole().getName();
        }
    }

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    private User getUser() {
        String name = this.getPrincipal();
        if (name.contains("anonymousUser")) {
            return null;
        } else {
            return userService.findByName(name);
        }
    }

    @GetMapping()
    public ModelAndView showCart() {
        ModelAndView modelAndView = new ModelAndView("/eshopper/cart");
        Iterable<Cart> carts = cartService.findAllByOrderNumberAndUser(this.getUser().getOrderNumber(), this.getUser());
        modelAndView.addObject("carts", carts);
        Long total = Long.valueOf(0);
        for (Cart cart : carts) {
            Product product = productService.findByProductId(cart.getProduct().getProductId());
            if (product.getQuantity() < cart.getQuantity()) {
                cart.setQuantity(product.getQuantity());
                cartService.save(cart);
            }
            total += cart.getQuantity() * cart.getProduct().getPrice();
        }
        modelAndView.addObject("total", total);

        return modelAndView;
    }

//    @GetMapping("/addCart/{productId}")
//    public ModelAndView addCart(@PathVariable("productId") Long productId, @RequestParam("s") Optional<String> keyword, @RequestParam("page") Optional<Integer> page) {
//        ModelAndView modelAndView = productController.index(keyword, page);
//        if (this.getUser() == null) {
//            modelAndView.addObject("message","Vui long dang nhap de mua hang");
//            return modelAndView;
//        } else {
//            Cart cart = cartService.findByProductAndUser(productService.findByProductId(productId), this.getUser());
//            if(cart != null) {
//                cart.setQuantity(cart.getQuantity() + 1);
//                cartService.save(cart);
//                Long countOrder = cartService.countByOrderNumberAndUser(this.getUser().getOrderNumber(), this.getUser());
//                modelAndView.addObject("countOrder", countOrder);
//                return modelAndView;
//            }
//            cart = new Cart();
//            if(this.getUser().getOrderNumber() == null) {
//                this.getUser().setOrderNumber(Long.valueOf(1));
//            }
//            cart.setOrderNumber(this.getUser().getOrderNumber());
//            cart.setQuantity(Long.valueOf(1));
//            cart.setProduct(productService.findByProductId(productId));
//            cart.setUser(this.getUser());
//            cartService.save(cart);
//            Long countOrder = cartService.countByOrderNumberAndUser(this.getUser().getOrderNumber(), this.getUser());
//            modelAndView.addObject("countOrder", countOrder);
//            return modelAndView;
//        }
//    }

    @GetMapping("/addCart/{productId}")
    public ModelAndView addCart(@PathVariable Long productId) {
//        ModelAndView modelAndView = new ModelAndView("/eshopper/cart");
        Cart cart = cartService.findByProductAndUserAndOrderNumber(productService.findByProductId(productId), this.getUser(), this.getUser().getOrderNumber());
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + 1);
            cartService.save(cart);
            return this.showCart();
//            Long countOrder = cartService.countByOrderNumberAndUser(this.getUser().getOrderNumber(), this.getUser());
        }
        cart = new Cart();
        if(this.getUser().getOrderNumber() == null) {
            this.getUser().setOrderNumber(Long.valueOf(1));
        }
        cart.setOrderNumber(this.getUser().getOrderNumber());
        cart.setQuantity(Long.valueOf(1));
        cart.setProduct(productService.findByProductId(productId));
        cart.setUser(this.getUser());
        cartService.save(cart);
        return this.showCart();
    }

    @GetMapping("/addcart/{productId}")
    public ResponseEntity<Long> add(@PathVariable Long productId) {
        Cart cart = cartService.findByProductAndUserAndOrderNumber(productService.findByProductId(productId), this.getUser(), this.getUser().getOrderNumber());
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + 1);
            cartService.save(cart);
            Long countOrder = cartService.countByOrderNumberAndUser(this.getUser().getOrderNumber(), this.getUser());
            return new ResponseEntity<>(countOrder, HttpStatus.OK);
        }
        cart = new Cart();
        if(this.getUser().getOrderNumber() == null) {
            this.getUser().setOrderNumber(Long.valueOf(1));
        }
        cart.setOrderNumber(this.getUser().getOrderNumber());
        cart.setQuantity(Long.valueOf(1));
        cart.setProduct(productService.findByProductId(productId));
        cart.setUser(this.getUser());
        cartService.save(cart);
        Long countOrder = cartService.countByOrderNumberAndUser(this.getUser().getOrderNumber(), this.getUser());
        return new ResponseEntity<>(countOrder, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<Cart> delete(@PathVariable Long cartId) {
            cartService.remove(cartId);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/quantity/{cartId}")
    public ResponseEntity<Cart> increase(@PathVariable Long cartId, @RequestBody Long quantity) {
        Cart cart = cartService.findByCartId(cartId);
        cart.setQuantity(quantity);
        cartService.save(cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("cart/index");
        modelAndView.addObject("carts", cartService.findAll());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("create");
        return modelAndView;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("cart") Cart cart){
        cartService.save(cart);
        return "redirect:/cart/list";
    }

    @GetMapping("/buy")
    public String buy(){

        Iterable<Cart> carts = cartService.findAllByOrderNumberAndUser(this.getUser().getOrderNumber(), this.getUser());
        Long sum = Long.valueOf(0);
        for(Cart cart : carts){
            Product product = productService.findByProductId(cart.getProduct().getProductId());
            product.setQuantity(product.getQuantity() - cart.getQuantity());
            productService.save(product);
            sum += cart.getQuantity() * cart.getProduct().getPrice();
        }
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderNumber(this.getUser().getOrderNumber());
        orderDetails.setTotalPrice(sum);
        orderDetailsService.save(orderDetails);
        this.getUser().setOrderNumber(this.getUser().getOrderNumber() + 1);
        userService.save(this.getUser());
        return "redirect:/product";
    }
}
