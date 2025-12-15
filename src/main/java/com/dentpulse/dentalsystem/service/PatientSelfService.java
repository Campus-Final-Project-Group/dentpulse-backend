package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.config.JwtUtil;
import com.dentpulse.dentalsystem.dto.PatientListDto;
import com.dentpulse.dentalsystem.dto.PatientProfileDto;
import com.dentpulse.dentalsystem.dto.UpdatePatientRequest;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.User;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientSelfService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private JwtUtil jwtUtil;

    //  GET PROFILE (Temporary: get first patient of this user)
    public PatientProfileDto getMyProfile(String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) throw new RuntimeException("User not found!");

        List<Patient> patients = patientRepo.findAllByUserId(user.getId());
        if (patients == null || patients.isEmpty()) {
            throw new RuntimeException("No patient profile found for this user!");
        }

        Patient patient = patients.get(0); // TEMPORARY: later we will choose by patientId

        PatientProfileDto dto = new PatientProfileDto();
        dto.setFullName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getContact());
        dto.setBirthDate(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : null);
        dto.setAddress(patient.getAddress());

        return dto;
    }

    //  UPDATE PROFILE (Temporary: update first patient of this user)
    public PatientProfileDto updateProfile(String token, UpdatePatientRequest req) {
        String email = jwtUtil.extractEmail(token);

        User user = userRepo.findByEmail(email);
        if (user == null) throw new RuntimeException("User not found!");

        List<Patient> patients = patientRepo.findAllByUserId(user.getId());
        if (patients == null || patients.isEmpty()) {
            throw new RuntimeException("No patient profile found for this user!");
        }

        Patient patient = patients.get(0); // TEMPORARY: later we will choose by patientId

        // update user fields
        user.setUserName(req.getFullName());
        user.setContact(req.getPhone());
        userRepo.save(user);

        // update patient fields
        if (req.getBirthDate() != null && !req.getBirthDate().isBlank()) {
            patient.setDateOfBirth(LocalDate.parse(req.getBirthDate()));
        }  // ✔ Convert String → LocalDate
        patient.setAddress(req.getAddress());
        patientRepo.save(patient);

        return getMyProfile(token);
    }

    public List<PatientListDto> getMyPatients(String token) {

        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        List<Patient> patients = patientRepo.findAllByUserId(user.getId());
        List<PatientListDto> result = new ArrayList<>();

        for (Patient p : patients) {

            PatientListDto dto = new PatientListDto();
            dto.setPatientId(p.getId());
            dto.setFullName(user.getUserName()); // temporary
            dto.setBirthDate(
                    p.getDateOfBirth() != null
                            ? p.getDateOfBirth().toString()
                            : null
            );

            result.add(dto);
        }

        return result;

    }


}
