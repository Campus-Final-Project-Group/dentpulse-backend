package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Count items for "Low Stock" card
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantity < i.minStock AND i.quantity > 0")
    long countLowStockItems();

    // FIXED: Count items for "Out of Stock" card
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantity = 0")
    long countOutOfStock();

    // Calculate sum for "Total Value" card
    @Query("SELECT SUM(i.quantity * i.price) FROM Inventory i")
    Double calculateTotalInventoryValue();

    // Search logic for SearchBar
    @Query("SELECT i FROM Inventory i WHERE " +
            "LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.sku) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.brand) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Inventory> searchInventory(@Param("query") String query);
}