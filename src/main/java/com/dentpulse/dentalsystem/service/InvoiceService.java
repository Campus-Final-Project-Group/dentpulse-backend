package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.InvoiceResponseDto;
import com.dentpulse.dentalsystem.entity.Invoice;
import com.dentpulse.dentalsystem.entity.InvoiceStatus;
import com.dentpulse.dentalsystem.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepo;

    public List<InvoiceResponseDto> getAllInvoices() {

        List<Invoice> invoices = invoiceRepo.findAll();
        List<InvoiceResponseDto> response = new ArrayList<>();

        for (Invoice invoice : invoices) {
            InvoiceResponseDto dto = new InvoiceResponseDto();
            dto.setInvoiceId(invoice.getId());
            dto.setInvoiceNumber(invoice.getInvoiceNumber());
            dto.setPatientName(invoice.getPatient().getFullName());
            dto.setDescription(invoice.getDescription());
            dto.setAmount(invoice.getAmount());
            dto.setStatus(invoice.getStatus().name());
            dto.setPaymentMethod(invoice.getPaymentMethod().name());
            dto.setInvoiceDate(invoice.getInvoiceDate().toString());

            response.add(dto);
        }

        return response;
    }

    public Map<String, Object> getBillingStats() {

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalInvoices", invoiceRepo.count());
        stats.put("paidInvoices", invoiceRepo.countByStatus(InvoiceStatus.PAID));
        stats.put("pendingInvoices", invoiceRepo.countByStatus(InvoiceStatus.PENDING));

        Double revenue = invoiceRepo.getTotalRevenue();
        stats.put("totalRevenue", revenue != null ? revenue : 0);

        return stats;
    }
}

