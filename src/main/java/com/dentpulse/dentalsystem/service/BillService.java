package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.BillRequestDto;
import com.dentpulse.dentalsystem.dto.BillResponseDto;
import com.dentpulse.dentalsystem.entity.Bill;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.TreatmentService;
import com.dentpulse.dentalsystem.repository.BillRepository;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.TreatmentServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final TreatmentServiceRepository treatmentServiceRepository;
    private final PatientRepository patientRepository;



    //CREATE BILL (date allowed only here)
    public BillResponseDto createBill(BillRequestDto dto) {

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        TreatmentService service = treatmentServiceRepository
                .findById(dto.getTreatmentServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Bill bill = new Bill();
        bill.setPatient(patient);
        bill.setBillDate(dto.getBillDate());
        bill.setTreatmentService(service);
        bill.setAmount(service.getCost());

        bill.setBillNumber("INV-" + System.currentTimeMillis());
        bill.setStatus("UNPAID");
        bill.setPaymentMethod("Cash");

        billRepository.save(bill);

        return mapToDto(bill);
    }


    // 📋 GET ALL BILLS
    public List<BillResponseDto> getAllBills() {
        return billRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    //UPDATE BILL (NO DATE UPDATE)
    public BillResponseDto updateBill(Long id, BillRequestDto dto) {

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        //Update treatment only
        if (dto.getTreatmentServiceId() != null) {
            TreatmentService treatment = treatmentServiceRepository
                    .findById(dto.getTreatmentServiceId())
                    .orElseThrow(() -> new RuntimeException("Treatment not found"));

            bill.setTreatmentService(treatment);
            bill.setAmount(treatment.getCost());
        }

        return mapToDto(billRepository.save(bill));
    }


    //DELETE BILL
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    //MAPPER (single source of truth)
    private BillResponseDto mapToDto(Bill bill) {
        return new BillResponseDto(
                bill.getId(),
                bill.getBillNumber(),
                bill.getPatient().getFullName(),
                bill.getTreatmentService().getDescription(),
                bill.getAmount(),
                bill.getStatus(),
                bill.getPaymentMethod(),
                bill.getBillDate().toString()
        );
    }
}