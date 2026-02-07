package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.BillRequestDto;
import com.dentpulse.dentalsystem.dto.BillResponseDto;
import com.dentpulse.dentalsystem.entity.Bill;
import com.dentpulse.dentalsystem.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;

    // ➕ CREATE
    public BillResponseDto createBill(BillRequestDto dto) {
        Bill bill = Bill.builder()
                .billNumber("BILL-" + UUID.randomUUID().toString().substring(0, 8))
                .patientName(dto.getPatientName())
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .billDate(dto.getBillDate())
                .paymentMethod("Cash")
                .build();

        return mapToDto(billRepository.save(bill));
    }

    // 📋 TABLE
    public List<BillResponseDto> getAllBills() {
        return billRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // 📅 FILTER BY DATE
    public List<BillResponseDto> getBillsByDate(LocalDate date) {
        return billRepository.findByBillDate(date)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // 🔁 MAPPER
    private BillResponseDto mapToDto(Bill bill) {
        return new BillResponseDto(
                bill.getId(),
                bill.getBillNumber(),
                bill.getPatientName(),
                bill.getDescription(),
                bill.getAmount(),
                bill.getPaymentMethod(),
                bill.getBillDate().toString()
        );
    }
    // 🗑️ DELETE BILL
    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new RuntimeException("Bill not found with id: " + id);
        }
        billRepository.deleteById(id);
    }

}
