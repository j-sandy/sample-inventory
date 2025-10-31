package com.example.inventorymanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String itemCode;
    private String image;
    private String description;
    private Integer quantity;
    private LocalDate procurementDate;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
}
