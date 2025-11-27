package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.config.JwtUtil;
import com.dentpulse.dentalsystem.dto.PatientProfileDto;
import com.dentpulse.dentalsystem.dto.UpdatePatientRequest;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.User;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PatientSelfService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private JwtUtil jwtUtil;

    // GET PROFILE
    public PatientProfileDto getMyProfile(String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        Patient patient = patientRepo.findByUserId(user.getId());

        PatientProfileDto dto = new PatientProfileDto();
        dto.setFullName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getContact());
        dto.setBirthDate(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : null);
        dto.setAddress(patient.getAddress());

        return dto;
    }

    // UPDATE PROFILE
    public PatientProfileDto updateProfile(String token, UpdatePatientRequest req) {
        String email = jwtUtil.extractEmail(token);

        User user = userRepo.findByEmail(email);
        Patient patient = patientRepo.findByUserId(user.getId());

        user.setUserName(req.getFullName());
        user.setContact(req.getPhone());
        userRepo.save(user);

        // UPDATE PATIENT SECTION (correct version)
        patient.setDateOfBirth(LocalDate.parse(req.getBirthDate()));  // ✔ Convert String → LocalDate
        patient.setAddress(req.getAddress());
        patientRepo.save(patient);

        return getMyProfile(token);
    }
}
