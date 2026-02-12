package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.InventorySummaryDTO;
import com.dentpulse.dentalsystem.entity.Inventory;
import com.dentpulse.dentalsystem.entity.Medicine;
import com.dentpulse.dentalsystem.entity.MedicineStatus;
import com.dentpulse.dentalsystem.repository.InventoryRepository;
import com.dentpulse.dentalsystem.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    private String determineStatus(int quantity, int minStock) {
        if (quantity <= 0) {
            return "OUT OF STOCK";
        } else if (quantity < minStock) {
            return "LIMITED";
        } else {
            return "AVAILABLE";
        }
    }

    @GetMapping("/search")
    public List<Inventory> searchItems(@RequestParam String query) {
        return inventoryRepository.searchInventory(query);
    }

    @GetMapping("/stats")
    public InventorySummaryDTO getInventoryStats() {
        long total = inventoryRepository.count();
        long low = inventoryRepository.countLowStockItems();
        long out = inventoryRepository.countOutOfStock();
        Double totalVal = inventoryRepository.calculateTotalInventoryValue();

        return new InventorySummaryDTO(
                total,
                low,
                out,
                totalVal != null ? totalVal : 0.0
        );
    }

    @GetMapping
    public List<Inventory> getAllItems() {
        return inventoryRepository.findAll();
    }

    @PostMapping
    @Transactional
    public Inventory addItem(@RequestBody Inventory item) {
        String status = determineStatus(item.getQuantity(), item.getMinStock());
        item.setMedicineStatus(status);

        if ("Medicine".equalsIgnoreCase(item.getCategory())) {
            Medicine med = new Medicine();
            med.setMedicineName(item.getName());
            med.setBrand(item.getBrand());
            med.setQuantity(item.getQuantity());
            med.setDosage(item.getDosage()); // Dosage added only if it's a medicine

            try {
                med.setMedicineStatus(MedicineStatus.valueOf(status.replace(" ", "_")));
            } catch (Exception e) {
                med.setMedicineStatus(MedicineStatus.AVAILABLE);
            }

            Medicine savedMed = medicineRepository.save(med);
            item.setMedicineId(savedMed.getMedicine_id().intValue());
        } else {
            item.setDosage(null);
        }
        return inventoryRepository.save(item);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Inventory> updateItem(@PathVariable Long id, @RequestBody Inventory details) {
        Inventory item = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setName(details.getName());
        item.setSku(details.getSku());
        item.setCategory(details.getCategory());
        item.setQuantity(details.getQuantity());
        item.setMinStock(details.getMinStock());
        item.setUnit(details.getUnit());
        item.setPrice(details.getPrice());
        item.setBrand(details.getBrand());
        item.setExpiryDate(details.getExpiryDate());

        String status = determineStatus(details.getQuantity(), details.getMinStock());
        item.setMedicineStatus(status);

        if ("Medicine".equalsIgnoreCase(details.getCategory())) {
            item.setDosage(details.getDosage()); // Dosage updated only for medicines
            if (item.getMedicineId() != null) {
                medicineRepository.findById(item.getMedicineId().longValue()).ifPresent(med -> {
                    med.setMedicineName(details.getName());
                    med.setBrand(details.getBrand());
                    med.setQuantity(details.getQuantity());
                    med.setDosage(details.getDosage());
                    try {
                        med.setMedicineStatus(MedicineStatus.valueOf(status.replace(" ", "_")));
                    } catch (Exception e) {}
                    medicineRepository.save(med);
                });
            }
        } else {
            item.setDosage(null);
            item.setMedicineId(null); // If category changed from Medicine, remove link
        }

        return ResponseEntity.ok(inventoryRepository.save(item));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        Inventory item = inventoryRepository.findById(id).orElse(null);

        if (item != null) {
            // Check if there is a linked medicine to delete first
            if (item.getMedicineId() != null) {
                medicineRepository.deleteById(item.getMedicineId().longValue());
            }
            // Now delete from your inventory table
            inventoryRepository.delete(item);
        }

        return ResponseEntity.ok().build();
    }
}