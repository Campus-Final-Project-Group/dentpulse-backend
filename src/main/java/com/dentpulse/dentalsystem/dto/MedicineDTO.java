package com.dentpulse.dentalsystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDTO {

    private Long medicine_id;
    private String medicineName;
    private String dosage;
    private String brand;
    private int quantity;
    private String medicineStatus;
}
