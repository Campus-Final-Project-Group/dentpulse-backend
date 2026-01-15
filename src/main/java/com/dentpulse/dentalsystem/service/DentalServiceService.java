package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.entity.DentalService;
import com.dentpulse.dentalsystem.repository.DentalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DentalServiceService {

    private final DentalServiceRepository repository;

    public DentalService create(DentalService service) {
        return repository.save(service);
    }

    public List<DentalService> getAll() {
        return repository.findAll();
    }

    public DentalService getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dental service not found"));
    }

    public DentalService update(Long id, DentalService updated) {
        DentalService existing = getById(id);

        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setDurationMin(updated.getDurationMin());
        existing.setPrice(updated.getPrice());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}