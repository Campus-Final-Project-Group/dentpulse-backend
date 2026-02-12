package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.BillRequestDto;
import com.dentpulse.dentalsystem.dto.BillResponseDto;
import com.dentpulse.dentalsystem.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    // 📋 Get ALL bills (no date)
    @GetMapping
    public List<BillResponseDto> getAllBills() {
        return billService.getAllBills();
    }

    // 🗑️ Delete
    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
    }
    @PutMapping("/{id}")
    public BillResponseDto updateBill(
            @PathVariable Long id,
            @RequestBody BillRequestDto dto
    ) {
        return billService.updateBill(id, dto);
    }


}
