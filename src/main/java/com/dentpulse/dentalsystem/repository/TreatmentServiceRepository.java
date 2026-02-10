package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.TreatmentService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentServiceRepository extends JpaRepository<TreatmentService, Long> {
    boolean existsByServiceName(String serviceName);
}

