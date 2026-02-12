package com.dentpulse.dentalsystem.controller;



import com.dentpulse.dentalsystem.dto.InventorySummaryDTO;

import com.dentpulse.dentalsystem.entity.Inventory;

import com.dentpulse.dentalsystem.repository.InventoryRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



import java.util.List;



@RestController

@RequestMapping("/api/v1/admin/inventory")

@CrossOrigin(origins = "*")

public class InventoryController {



    @Autowired

    private InventoryRepository inventoryRepository;



// --- SEARCH ---

    @GetMapping("/search")

    public List<Inventory> searchItems(@RequestParam String query) {

        return inventoryRepository.searchInventory(query);

    }



// --- DASHBOARD STATS ---

    @GetMapping("/stats")
    public InventorySummaryDTO getInventoryStats() {
        long total = inventoryRepository.count();
        long low = inventoryRepository.countLowStockItems();

        // UPDATED: Now matches the Repository method name
        long out = inventoryRepository.countOutOfStock();

        Double totalVal = inventoryRepository.calculateTotalInventoryValue();

        return new InventorySummaryDTO(
                total,
                low,
                out,
                totalVal != null ? totalVal : 0.0
        );
    }



// --- STANDARD CRUD ---

    @GetMapping

    public List<Inventory> getAllItems() {

        return inventoryRepository.findAll();

    }



    @PostMapping
    public Inventory addItem(@RequestBody Inventory item) {
        if (!"Medicine".equalsIgnoreCase(item.getCategory())) {
            item.setDosage(null);
        }
        return inventoryRepository.save(item);
    }



    @PutMapping("/{id}")

    public ResponseEntity<Inventory> updateItem(@PathVariable Long id, @RequestBody Inventory details) {

        Inventory item = inventoryRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("Item not found"));



// Mapping all fields from your React ItemDialog

        item.setName(details.getName());

        item.setSku(details.getSku());

        item.setCategory(details.getCategory());

        item.setQuantity(details.getQuantity());

        item.setMinStock(details.getMinStock());

        item.setUnit(details.getUnit());

        item.setPrice(details.getPrice());

        item.setBrand(details.getBrand());

        item.setExpiryDate(details.getExpiryDate());

        if ("Medicine".equalsIgnoreCase(details.getCategory())) {
            item.setDosage(details.getDosage());
        } else {
            item.setDosage(null); // Clean up dosage if category is changed to something else
        }



// Optional fields

        item.setMedicineId(details.getMedicineId());

        item.setMedicineStatus(details.getMedicineStatus());



        return ResponseEntity.ok(inventoryRepository.save(item));

    }



    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {

        inventoryRepository.deleteById(id);

        return ResponseEntity.ok().build();

    }

}