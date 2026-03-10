package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.entity.TreatmentService;
import com.dentpulse.dentalsystem.repository.TreatmentServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/treatment-services")
@RequiredArgsConstructor
@CrossOrigin
public class TreatmentServiceController {

    private final TreatmentServiceRepository repository;

    @GetMapping
    public List<TreatmentService> getAll() {
        return repository.findAll();
    }
}