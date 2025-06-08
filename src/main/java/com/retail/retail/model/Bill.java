package com.retail.retail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    private String id;
    private List<BillItem> items;
    private double totalAmount;
    private double gst;
    private double discount;
    private double netPayable;
    private LocalDateTime date;

    public void setBillNo(String substring) {
    }

    public void setTime(LocalTime now) {
    }
}

