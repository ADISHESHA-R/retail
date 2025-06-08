package com.retail.retail.service;

import com.retail.retail.model.Bill;
import com.retail.retail.model.BillItem;
import com.retail.retail.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductService productService;

    public Bill generateBill(List<BillItem> items) {
        double total = 0;
        for (BillItem item : items) {
            total += item.getTotalPrice();

            // Deduct stock for each item
            productService.deductStock(item.getProductId(), item.getQuantity());
        }

        double gst = total * 0.18;
        double discount = total >= 500 ? total * 0.10 : 0;
        double netPayable = total + gst - discount;

        Bill bill = new Bill();
        bill.setItems(items);
        bill.setTotalAmount(total);
        bill.setGst(gst);
        bill.setDiscount(discount);
        bill.setNetPayable(netPayable);
        bill.setDate(LocalDate.now().atStartOfDay());
        bill.setTime(LocalTime.now());
        bill.setBillNo(UUID.randomUUID().toString().substring(0, 8));

        billRepository.save(bill);
        return bill;
    }

    public List<Bill> getTodaySales() {
        return billRepository.findByDate(LocalDate.now());
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public double getTotalRevenue() {
        return billRepository.findAll().stream().mapToDouble(Bill::getNetPayable).sum();
    }

    public int getTotalCustomers() {
        return billRepository.findAll().size();
    }
}
