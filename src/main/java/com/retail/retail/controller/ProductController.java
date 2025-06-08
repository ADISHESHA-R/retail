package com.retail.retail.controller;

import com.retail.retail.model.Product;
import com.retail.retail.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping
    public String viewAll(Model model) {
        model.addAttribute("products", service.getAllProducts());
        return "index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/add")
    public String saveProduct(@ModelAttribute Product product) {
        service.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("product", service.getById(id));
        return "edit-product";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Product product) {
        service.updateProduct(product.getId(), product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        service.deleteProduct(id);
        return "redirect:/products";
    }
}
