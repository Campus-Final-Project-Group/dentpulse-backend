package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.MedicineDTO;
import com.dentpulse.dentalsystem.entity.Medicine;
import com.dentpulse.dentalsystem.entity.MedicineStatus;
import com.dentpulse.dentalsystem.repository.MedicineRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private ModelMapper modelMapper;

    // 🔹 Get all medicines (ALL tab)
    public List<MedicineDTO> getAllMedicines() {
        List<Medicine> medicines = medicineRepo.findAll();
        return modelMapper.map(
                medicines,
                new TypeToken<List<MedicineDTO>>() {}.getType()
        );
    }

    // 🔹 Filter by status (Available / Limited / Out of Stock)
    public List<MedicineDTO> getMedicinesByStatus(String status) {

        MedicineStatus medicineStatus =
                MedicineStatus.valueOf(status.toUpperCase());

        List<Medicine> medicines =
                medicineRepo.findByMedicineStatus(medicineStatus);

        return modelMapper.map(
                medicines,
                new TypeToken<List<MedicineDTO>>() {}.getType()
        );
    }

    // 🔹 Search medicines (Search box)
    public List<MedicineDTO> searchMedicines(String keyword) {

        List<Medicine> medicines =
                medicineRepo.findByMedicineNameContainingIgnoreCase(keyword);

        return modelMapper.map(
                medicines,
                new TypeToken<List<MedicineDTO>>() {}.getType()
        );
    }
}
