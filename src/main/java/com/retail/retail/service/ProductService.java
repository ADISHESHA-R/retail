package com.retail.retail.service;

import com.retail.retail.model.Product;
import com.retail.retail.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getById(String id) {
        return productRepo.findById(id).orElse(null);
    }

    public void addProduct(Product product) {
        productRepo.save(product);
    }

    public void deleteProduct(String id) {
        productRepo.deleteById(id);
    }

    public void updateProduct(String id, Product updated) {
        Product p = getById(id);
        if (p != null) {
            p.setName(updated.getName());
            p.setPrice(updated.getPrice());
            p.setQuantity(updated.getQuantity());
            productRepo.save(p);
        }
    }

    public void deductStock(String id, int qty) {
        Product p = getById(id);
        if (p != null) {
            int newQty = p.getQuantity() - qty;
            if (newQty <= 0) {
                deleteProduct(id);
            } else {
                p.setQuantity(newQty);
                productRepo.save(p);
            }
        }
    }
}
