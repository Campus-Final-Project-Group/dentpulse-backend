package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.BillRequestDto;
import com.dentpulse.dentalsystem.dto.BillResponseDto;
import com.dentpulse.dentalsystem.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
@CrossOrigin
public class BillController {

    private final BillService billService;

    // ➕ Add bill
    @PostMapping
    public BillResponseDto createBill(@RequestBody BillRequestDto dto) {
        return billService.createBill(dto);
    }

    // 📋 Table
    @GetMapping("/table")
    public List<BillResponseDto> getAllBills() {
        return billService.getAllBills();
    }

    // 📅 Filter by date
    @GetMapping
    public List<BillResponseDto> getBillsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return billService.getBillsByDate(date);
    }

    // 🗑️ Delete
    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
    }
}
