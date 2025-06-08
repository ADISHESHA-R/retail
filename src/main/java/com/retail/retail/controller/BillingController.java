package com.retail.retail.controller;

import com.retail.retail.model.Bill;
import com.retail.retail.model.BillItem;
import com.retail.retail.model.Product;
import com.retail.retail.service.BillingService;
import com.retail.retail.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String billingPage(Model model) {
        model.addAttribute("products", productService.getAllProducts()
                .stream().filter(p -> p.getQuantity() > 0).toList());
        return "billing";
    }

    @PostMapping("/generate")
    public String generateBill(@RequestParam Map<String, String> params, Model model) {
        List<BillItem> items = new ArrayList<>();

        for (String key : params.keySet()) {
            if (key.startsWith("qty_")) {
                String id = key.substring(4);
                String value = params.get(key);

                if (value != null && !value.trim().isEmpty()) {
                    try {
                        int qty = Integer.parseInt(value);
                        if (qty > 0) {
                            Product p = productService.getById(id);
                            if (p != null) {
                                items.add(new BillItem(p.getId(), p.getName(), qty, p.getPrice(), qty * p.getPrice()));
                            }
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        }

        if (items.isEmpty()) {
            model.addAttribute("error", "Please enter quantity for at least one product.");
            model.addAttribute("products", productService.getAllProducts()
                    .stream().filter(p -> p.getQuantity() > 0).toList());
            return "billing";
        }

        Bill bill = billingService.generateBill(items);
        model.addAttribute("bill", bill);
        return "bill-receipt";
    }

    @GetMapping("/report")
    public String report(Model model) {
        List<Bill> bills = billingService.getTodaySales();
        double revenue = bills.stream().mapToDouble(Bill::getNetPayable).sum();
        model.addAttribute("bills", bills);
        model.addAttribute("totalCustomers", bills.size());
        model.addAttribute("totalRevenue", revenue);
        return "report";
    }
}
