package com.dentpulse.dentalsystem.config;

import com.dentpulse.dentalsystem.entity.TreatmentService;
import com.dentpulse.dentalsystem.entity.TreatmentType;
import com.dentpulse.dentalsystem.repository.TreatmentServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TreatmentServiceDataLoader {

    private final TreatmentServiceRepository repo;

    @Bean
    CommandLineRunner loadTreatmentServices() {
        return args -> {

            insertIfNotExists(
                    TreatmentType.EXTRACTION,
                    "Extraction (Normal)",
                    15,
                    2500
            );

            insertIfNotExists(
                    TreatmentType.EXTRACTION,
                    "Extraction (Difficult)",
                    30,
                    5000
            );

            insertIfNotExists(
                    TreatmentType.FILLING,
                    "Composite Filling",
                    20,
                    5000
            );

            insertIfNotExists(
                    TreatmentType.FILLING,
                    "Temporary Filling",
                    15,
                    2500
            );

            insertIfNotExists(
                    TreatmentType.FILLING,
                    "GIC Filling",
                    15,
                    4000
            );

            insertIfNotExists(
                    TreatmentType.ROOT_CANAL,
                    "Nerve Filling - Step 1",
                    15,
                    8500
            );

            insertIfNotExists(
                    TreatmentType.ROOT_CANAL,
                    "Nerve Filling - Step 2",
                    20,
                    8500
            );

            insertIfNotExists(
                    TreatmentType.ROOT_CANAL,
                    "Nerve Filling - Step 3",
                    20,
                    8000
            );

            insertIfNotExists(
                    TreatmentType.CLEANING,
                    "Scaling - Mild",
                    20,
                    4000
            );

            insertIfNotExists(
                    TreatmentType.CLEANING,
                    "Scaling - Moderate",
                    90,
                    10000
            );

            insertIfNotExists(
                    TreatmentType.CLEANING,
                    "Scaling - Severe",
                    20,
                    4000
            );

            insertIfNotExists(
                    TreatmentType.EXTRACTION,
                    "Removal Of Wisdom Tooth",
                    60,
                    15000
            );
            insertIfNotExists(
                    TreatmentType.CHECKUP,
                    "Checkup",
                    10,
                    2500
            );
        };
    }

    private void insertIfNotExists(
            TreatmentType type,
            String name,
            int time,
            double cost
    ) {
        if (!repo.existsByServiceName(name)) {
            TreatmentService service = new TreatmentService(
                    null,
                    type,
                    name,
                    time,
                    cost
            );
            repo.save(service);
        }
    }
}
