package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.MedicineDTO;
import com.dentpulse.dentalsystem.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping(value = "/api/medicine")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    // 🔹 ALL
    @GetMapping
    public List<MedicineDTO> getAllMedicines() {
        return medicineService.getAllMedicines();
    }

    // 🔹 Filter by status
    @GetMapping("/status/{status}")
    public List<MedicineDTO> getByStatus(@PathVariable String status) {
        return medicineService.getMedicinesByStatus(status);
    }

    // 🔹 Search
    @GetMapping("/search")
    public List<MedicineDTO> search(@RequestParam String keyword) {
        return medicineService.searchMedicines(keyword);
    }
}

