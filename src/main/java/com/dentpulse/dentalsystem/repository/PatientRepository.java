package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Get ALL patients of a user (self + family)
    List<Patient> findAllByUserId(Long userId);

    // Get ONE patient of a user (safe access)
    Optional<Patient> findByIdAndUserId(Long patientId, Long userId);

    // Get account owner (SELF patient)
    Patient findByUserIdAndAccountOwnerTrue(Long userId);

    // Get only family members (exclude account owner)
    List<Patient> findByUserIdAndAccountOwnerFalse(Long userId);



    Optional<Patient> findById(Long patientId);

    //find details for admin appointments
    List<Patient> findByIdOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(
            Long id,
            String fullName,
            String email,
            String phone
    );

    Optional<Patient> findByNic(String nic);
    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByUserIdAndFullNameIgnoreCaseAndDateOfBirth(
            Long userId,
            String fullName,
            LocalDate dateOfBirth
    );

    Optional<Patient> findByFullNameIgnoreCaseAndDateOfBirth(
            String fullName,
            LocalDate dateOfBirth
    );


}
