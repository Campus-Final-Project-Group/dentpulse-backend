package com.dentpulse.dentalsystem.dto;

import java.math.BigDecimal;
import java.util.List;

public class AdminDashboardSummaryDto {

    private long totalPatients;
    private long totalAppointments;
    private long inventoryItems;
    private BigDecimal  totalRevenue;

    // optional: today's appointments
    private List<?> todayAppointments;

    // getters and setters
    public long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public long getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(long inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal  totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<?> getTodayAppointments() {
        return todayAppointments;
    }

    public void setTodayAppointments(List<?> todayAppointments) {
        this.todayAppointments = todayAppointments;
    }
}