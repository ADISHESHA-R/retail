package com.retail.retail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillItem {
    private String productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}
