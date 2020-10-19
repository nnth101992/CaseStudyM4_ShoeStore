package com.casestudy.controller;


import com.casestudy.model.Category;
import com.casestudy.model.Product;
import com.casestudy.service.category.CategoryService;
import com.casestudy.service.product.ProductService;
import com.casestudy.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

//    private static String username = null;

    @Autowired
    Environment evn;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;
//    RoleService roleService;

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
        if (name.contains("anonymousUser")  ) {
            return null;
        } else {
            return userService.findByName(name).getRole().getName();
        }
    }


    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping()
    public ModelAndView index(@RequestParam("s") Optional<String> keyword, @RequestParam("page") Optional<Integer> page){
        ModelAndView modelAndView = new ModelAndView("/eshopper/index");
        Page<Product> products;
        int pageNum = 0;
        if (page.isPresent() && page.get() > 0) pageNum = page.get() - 1;
        Pageable pageRequest = PageRequest.of(pageNum, 9);
        if (keyword.isPresent()) {
            products = productService.findAllByNameContaining(keyword.get(), pageRequest);
            modelAndView.addObject( "keyword", keyword.get());
        } else {
            products = productService.findAll(pageRequest);
        }
        modelAndView.addObject("products", products);
        return modelAndView;


    }

    @GetMapping("/create")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }



    @PostMapping("/create")
    public ModelAndView createNewProduct(@ModelAttribute Product productForm){

        MultipartFile file = productForm.getImgFile();
        String image = file.getOriginalFilename();
        String fileUpload = evn.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(fileUpload + image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product();
        product.setName(productForm.getName());
        product.setImg(image);
        product.setDescription(productForm.getDescription());
        product.setPrice(productForm.getPrice());
        product.setQuantity(productForm.getQuantity());
        product.setCategory(productForm.getCategory());
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("product", new Product());
        modelAndView.addObject("message", "New product was created successfully!");
        return modelAndView;
    }

    @GetMapping("/list")
    public ModelAndView showList(@RequestParam("s") Optional<String> keyword, @RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("/product/list");
        Page<Product> products;
        int pageNum = 0;
        if (page.isPresent() && page.get() > 0) pageNum = page.get() - 1;
        Pageable pageRequest = PageRequest.of(pageNum, 10);
        if (keyword.isPresent()) {
            products = productService.findAllByNameContaining(keyword.get(), pageRequest);
            modelAndView.addObject("keyword", keyword.get());
        } else {
            products = productService.findAll(pageRequest);
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/edit-product/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Product product = productService.findByProductId(id);
        ModelAndView modelAndView;
        if(product != null) {
            modelAndView = new ModelAndView("/product/edit");
            modelAndView.addObject("product", product);
        } else {
            modelAndView = new ModelAndView("/error");
        }
        return modelAndView;
    }



    @PostMapping("/edit-product")
    public ModelAndView updateProduct(@ModelAttribute("product") Product product) {
        MultipartFile file = product.getImgFile();
        String image = file.getOriginalFilename();

        if (!image.equals("")) {
            String fileUpload = evn.getProperty("file_upload").toString();
            try {
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + image));
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.setImg(image);
        }
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        modelAndView.addObject("product", product);
        modelAndView.addObject("message", "Product updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete-product/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Product product = productService.findByProductId(id);
        ModelAndView modelAndView;
        if(product != null) {
            modelAndView = new ModelAndView("/product/delete");
            modelAndView.addObject("product", product);

        }else {
            modelAndView = new ModelAndView("/error");
        }
        return modelAndView;
    }

    @PostMapping("/delete-product")
    public String deleteProduct(@ModelAttribute("product") Product product){
        productService.remove(product.getProductId());
//        ModelAndView modelAndView = new ModelAndView("product/list");
////        modelAndView.addObject("message", "Failed Successfully");
//        return modelAndView;
        return "redirect:/product/list";
    }

    @GetMapping("/detail/{productId}")
    public ModelAndView showDetail(@PathVariable Long productId) {
        Product product = productService.findByProductId(productId);
        ModelAndView modelAndView = new ModelAndView("/eshopper/product-details");
        modelAndView.addObject("product", product);
        return modelAndView;
    }


    @GetMapping("/sort/Descending")
    public ModelAndView listProductWeak(@RequestParam("s") Optional<String> s){
        ModelAndView modelAndView= new ModelAndView("/eshopper/index");
        Sort sort= Sort.by("price");
        sort=sort.descending();
        Page<Product> products;
        if(s.isPresent()) {
            products = productService.findAllByNameContaining(s.get(), PageRequest.of(0, 9, sort));
        } else {
            products =  productService.findAll(PageRequest.of(0, 9, sort));
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }
    @GetMapping("/sort/Ascending")
    public ModelAndView listProductIncrease(@RequestParam("s") Optional<String> s){
        ModelAndView modelAndView= new ModelAndView("/eshopper/index");
        Sort sort= Sort.by("price");
        sort=sort.ascending();
        Page<Product> products;
        if(s.isPresent()) {
            products = productService.findAllByNameContaining(s.get(), PageRequest.of(0, 9, sort));
        } else {
            products =  productService.findAll(PageRequest.of(0, 9, sort));
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/category_product/{categoryId}")
    public ModelAndView showProductByCategory(@PathVariable Long categoryId,Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/eshopper/index");
        Category category = categoryService.findByCategoryId(categoryId);
        Page<Product> products = productService.findAllByCategory(category,pageable);
        modelAndView.addObject("products", products);
        return modelAndView;
    }

}
