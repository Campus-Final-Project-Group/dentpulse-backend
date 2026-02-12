package com.dentpulse.dentalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventorySummaryDTO {
    private long totalItems;
    private long lowStockCount;
    private long outOfStockCount;
    private double totalValue;
}