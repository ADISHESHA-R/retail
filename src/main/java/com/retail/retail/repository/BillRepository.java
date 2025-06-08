package com.retail.retail.repository;

import com.retail.retail.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository extends MongoRepository<Bill, String> {
    List<Bill> findByDate(LocalDate now);
}
