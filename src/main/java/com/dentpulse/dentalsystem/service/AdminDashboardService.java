package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.AdminDashboardSummaryDto;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import com.dentpulse.dentalsystem.repository.InvoiceRepository;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AdminDashboardService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private InvoiceRepository invoiceRepo;


    public AdminDashboardSummaryDto getDashboardSummary() {


        AdminDashboardSummaryDto dto = new AdminDashboardSummaryDto();

        dto.setTotalPatients(patientRepo.count());
        dto.setTotalAppointments(appointmentRepo.count());
        dto.setInventoryItems(medicineRepo.count());

        Double revenue = invoiceRepo.getTotalRevenue();

        BigDecimal formattedRevenue = BigDecimal
                .valueOf(revenue != null ? revenue : 0)
                .setScale(2, RoundingMode.HALF_UP);

        dto.setTotalRevenue(formattedRevenue);


        dto.setTodayAppointments(
                appointmentService.getTodayAppointments()
        );

        return dto;
    }
}