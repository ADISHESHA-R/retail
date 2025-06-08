package com.retail.retail.repository;

import com.retail.retail.model.Bill;
import com.retail.retail.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}

