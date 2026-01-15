package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dental_services")
@Data
public class DentalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer durationMin;

    @Column(nullable = false)
    private Double price;
}
