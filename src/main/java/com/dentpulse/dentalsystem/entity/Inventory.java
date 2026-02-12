package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "inventory")
public class Inventory {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id") // Maps Java 'id' to DB 'inventory_id'
    private Long id;

    @Column(name = "item_name")
    private String name;

    private String sku;
    private String category;
    private Integer quantity;

    @Column(name = "min_stock")
    private Integer minStock;

    private String unit;

    @Column(name = "price_lkr")
    private Double price;

    private String brand;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    // Optional: Kept for consistency with previous medicine requirements

    @Column(name = "medicine_id", nullable = true) // Ensure nullable is true
    private Integer medicineId;

    @Column(name = "medicine_status")
    private String medicineStatus;

    // Inside com.dentpulse.dentalsystem.entity.Inventory.java

    @Column(name = "dosage")
    private String dosage; // New field for medicine dosage (e.g., 500mg)
}