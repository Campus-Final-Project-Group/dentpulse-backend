package com.dentpulse.dentalsystem.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InventoryDTO {
    private Long id;
    private String name;
    private String sku;
    private String category;
    private Integer quantity;
    private Integer minStock;
    private String unit;
    private Double price;
    private String brand;
    private LocalDate expiryDate;
}