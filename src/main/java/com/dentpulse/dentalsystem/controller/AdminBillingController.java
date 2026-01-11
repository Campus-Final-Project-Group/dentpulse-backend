package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.InvoiceResponseDto;
import com.dentpulse.dentalsystem.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/billing")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBillingController {

    @Autowired
    private InvoiceService invoiceService;

    // Summary cards
    @GetMapping("/stats")
    public Map<String, Object> getBillingStats() {
        return invoiceService.getBillingStats();
    }

    // Invoice table
    @GetMapping("/invoices")
    public List<InvoiceResponseDto> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }
}

