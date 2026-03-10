package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.AdminDashboardSummaryDto;
import com.dentpulse.dentalsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class AdminDashboardService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private MedicineRepository medicineRepo; // Kept so her logic doesn't break

    @Autowired
    private InventoryRepository inventoryRepo; // Added to fix your card

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private BillRepository billRepo;

    public AdminDashboardSummaryDto getDashboardSummary() {

        AdminDashboardSummaryDto dto = new AdminDashboardSummaryDto();

        dto.setTotalPatients(patientRepo.count());

        int todayCount = appointmentService.getTodayAppointments().size();
        dto.setTodayAppointmentCount(todayCount);

        // --- ONLY CHANGE IS HERE ---
        // This makes YOUR card show the Inventory count.
        dto.setInventoryItems(inventoryRepo.count());
        // ---------------------------

        Double todayRevenue = billRepo.getTodayRevenue(LocalDate.now());

        BigDecimal formattedRevenue = BigDecimal
                .valueOf(todayRevenue != null ? todayRevenue : 0)
                .setScale(2, RoundingMode.HALF_UP);

        dto.setTodayRevenue(formattedRevenue);

        dto.setTodayAppointments(
                appointmentService.getTodayAppointments()
        );

        return dto;
    }
}