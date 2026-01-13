package com.dentpulse.dentalsystem.dto;

import java.math.BigDecimal;
import java.util.List;

public class AdminDashboardSummaryDto {

    private long totalPatients;
    private long todayAppointmentCount;
    private long inventoryItems;
    private BigDecimal  todayRevenue;

    public List<?> getTodayAppointments() {
        return todayAppointments;
    }

    public void setTodayAppointments(List<?> todayAppointments) {
        this.todayAppointments = todayAppointments;
    }

    public BigDecimal getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(BigDecimal todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public long getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(long inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public long getTodayAppointmentCount() {
        return todayAppointmentCount;
    }

    public void setTodayAppointmentCount(long todayAppointmentCount) {
        this.todayAppointmentCount = todayAppointmentCount;
    }

    public long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }

    // optional: today's appointments
    private List<?> todayAppointments;

}