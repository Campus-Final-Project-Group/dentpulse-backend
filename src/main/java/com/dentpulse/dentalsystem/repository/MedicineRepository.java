package com.dentpulse.dentalsystem.repository;



import com.dentpulse.dentalsystem.entity.Medicine;
import com.dentpulse.dentalsystem.entity.MedicineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // Hibernate auto-generates SQL
    List<Medicine> findByMedicineStatus(MedicineStatus status);

    List<Medicine> findByMedicineNameContainingIgnoreCase(String name);

    // ✅ NEW (for prescription dropdown)
    List<Medicine> findByMedicineStatusIn(List<MedicineStatus> allowedStatuses);


}

