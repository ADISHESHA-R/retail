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
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BillingService billingService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        populateDashboard(model);
        return "dashboard";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/dashboard";
    }

    @PostMapping("/generate-bill")
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
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        if (items.isEmpty()) {
            model.addAttribute("error", "Please enter quantity for at least one product.");
            populateDashboard(model);
            return "dashboard";
        }

        Bill bill = billingService.generateBill(items);
        model.addAttribute("bill", bill);
        return "bill-receipt";
    }

    private void populateDashboard(Model model) {
        List<Bill> bills = billingService.getAllBills();
        List<String> billDates = bills.stream().map(b -> b.getDate().toString()).collect(Collectors.toList());
        List<Double> billAmounts = bills.stream().map(Bill::getNetPayable).collect(Collectors.toList());

        model.addAttribute("products", productService.getAllProducts()
                .stream().filter(p -> p.getQuantity() > 0).toList());
        model.addAttribute("bills", bills);
        model.addAttribute("totalRevenue", billingService.getTotalRevenue());
        model.addAttribute("totalCustomers", billingService.getTotalCustomers());
        model.addAttribute("billDates", billDates);
        model.addAttribute("billAmounts", billAmounts);
    }
}
