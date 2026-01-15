package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.entity.DentalService;
import com.dentpulse.dentalsystem.service.DentalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dental-services")
@RequiredArgsConstructor
public class DentalServiceController {

    private final DentalServiceService service;

    // ADMIN ONLY
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DentalService create(@RequestBody DentalService dentalService) {
        return service.create(dentalService);
    }

    // ADMIN + PATIENT
    @GetMapping
    public List<DentalService> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DentalService getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ADMIN ONLY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DentalService update(
            @PathVariable Long id,
            @RequestBody DentalService dentalService
    ) {
        return service.update(id, dentalService);
    }

    // ADMIN ONLY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
