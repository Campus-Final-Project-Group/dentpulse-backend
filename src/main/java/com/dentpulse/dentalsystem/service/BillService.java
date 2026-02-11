package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.BillRequestDto;
import com.dentpulse.dentalsystem.dto.BillResponseDto;
import com.dentpulse.dentalsystem.entity.Bill;
import com.dentpulse.dentalsystem.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;

    // ➕ CREATE BILL (date allowed only here)
    public BillResponseDto createBill(BillRequestDto dto) {
        Bill bill = Bill.builder()
                .billNumber("BILL-" + UUID.randomUUID().toString().substring(0, 8))
                .patientName(dto.getPatientName())
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .billDate(dto.getBillDate())
                .status("Unpaid")
                .paymentMethod("Cash")
                .build();

        return mapToDto(billRepository.save(bill));
    }

    // 📋 GET ALL BILLS
    public List<BillResponseDto> getAllBills() {
        return billRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ✏️ UPDATE BILL (NO DATE UPDATE)
    public BillResponseDto updateBill(Long id, BillRequestDto dto) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        bill.setPatientName(dto.getPatientName());
        bill.setDescription(dto.getDescription());
        bill.setAmount(dto.getAmount());

        return mapToDto(billRepository.save(bill));
    }

    // 🗑️ DELETE BILL
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    // 🔁 MAPPER (single source of truth)
    private BillResponseDto mapToDto(Bill bill) {
        return new BillResponseDto(
                bill.getId(),
                bill.getBillNumber(),
                bill.getPatientName(),
                bill.getDescription(),
                bill.getAmount(),
                bill.getStatus(),
                bill.getPaymentMethod(),
                bill.getBillDate().toString()
        );
    }
}
