package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.entity.Prescription;
import com.dentpulse.dentalsystem.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository repository;

    public int incrementTodayCount() {
        LocalDate today = LocalDate.now();

        Prescription record =
                repository.findById(today)
                        .orElse(new Prescription(today, 0));

        record.setCount(record.getCount() + 1);
        repository.save(record);

        return record.getCount();
    }

    public int getTodayCount() {
        return repository.findById(LocalDate.now())
                .map(Prescription::getCount)
                .orElse(0);
    }
}


