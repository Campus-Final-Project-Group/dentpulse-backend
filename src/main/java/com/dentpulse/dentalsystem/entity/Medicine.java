package com.dentpulse.dentalsystem.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicine_id;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "dosage",nullable = false)
    private String dosage;    // 500mg

    @Column(name = "brand",nullable = false)
    private String brand;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "medicine_status")
    private MedicineStatus medicineStatus;
}
