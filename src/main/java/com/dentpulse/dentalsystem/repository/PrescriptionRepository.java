package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, LocalDate> {
}

