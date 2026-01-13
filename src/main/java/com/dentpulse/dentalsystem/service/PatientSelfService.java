package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.config.JwtUtil;
import com.dentpulse.dentalsystem.dto.*;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.User;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
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
    //  GET PROFILE (Account owner only)
    public PatientProfileDto getMyProfile(String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) throw new RuntimeException("User not found!");


        //  Always get account owner patient
        Patient patient = patientRepo
                .findByUserIdAndAccountOwnerTrue(user.getId());

        if (patient == null) {
            throw new RuntimeException("Account owner patient not found!");
        }
        //List<Patient> patients = patientRepo.findAllByUserId(user.getId());

        /*if (patients == null || patients.isEmpty()) {
            throw new RuntimeException("No patient profile found for this user!");
        }*/

        //Patient patient = patients.get(0); // TEMPORARY: later we will choose by patientId

        PatientProfileDto dto = new PatientProfileDto();
        dto.setFullName(patient.getFullName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setGender(patient.getGender());

        dto.setBirthDate(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : null);
        dto.setAddress(patient.getAddress());

        return dto;
    }

    //  UPDATE PROFILE (Temporary: update first patient of this user)
    public PatientProfileDto updateProfile(String token, UpdatePatientRequest req) {
        String email = jwtUtil.extractEmail(token);

        User user = userRepo.findByEmail(email);
        if (user == null) throw new RuntimeException("User not found!");

        /*List<Patient> patients = patientRepo.findAllByUserId(user.getId());
        if (patients == null || patients.isEmpty()) {
            throw new RuntimeException("No patient profile found for this user!");
        }

        Patient patient = patients.get(0); // TEMPORARY: later we will choose by patientId
        */
        Patient patient =
                patientRepo.findByUserIdAndAccountOwnerTrue(user.getId());

        if (patient == null) {
            throw new RuntimeException("Account owner patient not found!");
        }

        //  BLOCK email change (even if frontend sends it)
        if (req.getEmail() != null && !req.getEmail().equals(patient.getEmail())) {
            throw new RuntimeException("Email cannot be changed");
        }

        //  BLOCK phone change
        if (req.getPhone() != null && !req.getPhone().equals(patient.getPhone())) {
            throw new RuntimeException("Phone number cannot be changed");
        }

        /*
        // update user fields
        user.setUserName(req.getFullName());
        user.setContact(req.getPhone());
        userRepo.save(user);
        */
        // update patient-owned fields only
        patient.setFullName(req.getFullName());
        patient.setGender(req.getGender());
        //patient.setPhone(req.getPhone());


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
            //dto.setFullName(user.getUserName()); // temporary
            //  correct: patient name, not user name
            dto.setFullName(p.getFullName());
            dto.setBirthDate(
                    p.getDateOfBirth() != null
                            ? p.getDateOfBirth().toString()
                            : null
            );

            //  relationship logic
            dto.setRelationship(
                    p.isAccountOwner() ? "You" : p.getRelationship()
            );

            result.add(dto);
        }

        return result;

    }

    public PatientProfileForTableDto getMyProfileFortabel(String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) throw new RuntimeException("User not found!");


        //  Always get account owner patient
        Patient patient = patientRepo
                .findByUserIdAndAccountOwnerTrue(user.getId());

        if (patient == null) {
            throw new RuntimeException("Account owner patient not found!");
        }
        //List<Patient> patients = patientRepo.findAllByUserId(user.getId());

        /*if (patients == null || patients.isEmpty()) {
            throw new RuntimeException("No patient profile found for this user!");
        }*/

        //Patient patient = patients.get(0); // TEMPORARY: later we will choose by patientId

        PatientProfileForTableDto dto = new PatientProfileForTableDto();
        dto.setPatientId(patient.getId());
        dto.setFullName(patient.getFullName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setGender(patient.getGender());
        dto.setAccountOwner(patient.isAccountOwner());
        dto.setRelationship(
                patient.isAccountOwner() ? "Account Owner" : patient.getRelationship()
        );


        return dto;
    }


    public List<FamilyMemberDto> getMyFamilyMembers(String token) {

        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        //List<Patient> patients = patientRepo.findAllByUserId(user.getId());
        List<Patient> familyMembers = patientRepo.findByUserIdAndAccountOwnerFalse(user.getId());

        List<FamilyMemberDto> result = new ArrayList<>();

        /*for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);

            FamilyMemberDto dto = new FamilyMemberDto();
            dto.setPatientId(p.getId());
            dto.setFullName(user.getUserName()); // later patient name
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getContact());
           // dto.setRelationship(i == 0 ? "Account Owner" : "Other");
           // dto.setAccountOwner(i == 0); // first patient = SELF

            dto.setRelationship(
                    p.isAccountOwner() ? "Account Owner" : "Other"
            );
            dto.setAccountOwner(p.isAccountOwner());


            result.add(dto);
        }*/

        for (Patient p : familyMembers) {
            FamilyMemberDto dto = new FamilyMemberDto();
            dto.setPatientId(p.getId());
            dto.setFullName(p.getFullName());
            dto.setEmail(p.getEmail());
            dto.setPhone(p.getPhone());
            dto.setRelationship(p.getRelationship());
            dto.setAccountOwner(p.isAccountOwner());
            dto.setGender(p.getGender());
            dto.setBirthDate(p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : null);
            dto.setAddress(p.getAddress());

            result.add(dto);
        }

        return result;
    }

    public void addFamilyMember(String token, AddFamilyMemberRequest req) {

        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        Patient patient = new Patient();
        patient.setUser(user); // IMPORTANT: belongs to logged-in user
        /*patient.setAddress(req.getAddress());
        patient.setAccountOwner(false);*/

        // identity & profile data (FAMILY MEMBER)
        patient.setFullName(req.getFullName());
        patient.setEmail(req.getEmail());
        patient.setPhone(req.getPhone());
        patient.setRelationship(req.getRelationship());
        patient.setGender(req.getGender());

        patient.setAddress(req.getAddress());
        patient.setAccountOwner(false);

        if (req.getBirthDate() != null && !req.getBirthDate().isBlank()) {
            patient.setDateOfBirth(LocalDate.parse(req.getBirthDate()));
        }

        //========================== new changes from sahan ===========
        // Validate NIC rules
        validatePatient(
                req.getBirthDate(),
                req.getHasNic(),
                req.getNic()
        );

        // Prevent duplicates (only when no NIC/email)
        checkDuplicateChild(
                user,
                req.getFullName().trim(),
                LocalDate.parse(req.getBirthDate()),
                req.getNic(),
                req.getEmail()
        );

        // Set NIC definitively
        if (Boolean.TRUE.equals(req.getHasNic())) {
            patient.setNic(req.getNic());
        } else {
            patient.setNic(null);
        }

        if (req.getEmail() != null && req.getEmail().isBlank()) {
            patient.setEmail(null);
        }


        //==================================================================
        patientRepo.save(patient);
    }


    public void deleteFamilyMember(String token, Long patientId) {

        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        Patient patient = patientRepo
                .findByIdAndUserId(patientId, user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found!"));

        //  Prevent deleting account owner
        if (patient.isAccountOwner()) {
            throw new RuntimeException("Account owner cannot be deleted!");
        }

        patientRepo.delete(patient);
    }

    public void updateFamilyMember(String token, Long patientId, UpdateFamilyMemberRequest req) {

        String email = jwtUtil.extractEmail(token);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        Patient patient = patientRepo
                .findByIdAndUserId(patientId, user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found!"));

        //  Prevent editing account owner
        if (patient.isAccountOwner()) {
            throw new RuntimeException("Account owner cannot be edited!");
        }

        //  Update allowed family member fields
        patient.setFullName(req.getFullName());
        patient.setRelationship(req.getRelationship());
        patient.setPhone(req.getPhone());
        patient.setEmail(req.getEmail());
        patient.setAddress(req.getAddress());
        patient.setGender(req.getGender());

        if (req.getBirthDate() != null && !req.getBirthDate().isBlank()) {
            patient.setDateOfBirth(LocalDate.parse(req.getBirthDate()));
        }
        //========================== new changes from sahan ===========
        validatePatient(
                req.getBirthDate(),
                req.getHasNic(),
                req.getNic()
        );

        if (Boolean.TRUE.equals(req.getHasNic())) {
            patient.setNic(req.getNic());
        } else {
            patient.setNic(null); // 🔐 force clean data
        }

        if (req.getEmail() != null && req.getEmail().isBlank()) {
            patient.setEmail(null);
        }


        //==============================================================
        patientRepo.save(patient);
    }

// =========== For Admin Module ============
    public PatientProfileResponseDto createPatientByAdmin(PatientProfileDto dto) {
        Patient patient = new Patient();

        patient.setFullName(dto.getFullName());
        patient.setPhone(dto.getPhone());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());

        // Optional email
        patient.setEmail(dto.getEmail());

        // Convert String → LocalDate
        patient.setDateOfBirth(LocalDate.parse(dto.getBirthDate()));

        patient.setUser(null);              // No user account
        patient.setAccountOwner(false);     // Admin-created patient

        LocalDate dob = LocalDate.parse(dto.getBirthDate());

        checkDuplicateAdminPatient(
                dto.getFullName().trim(),
                dob,
                dto.getNic(),
                dto.getEmail()
        );

        validatePatient(
                dto.getBirthDate(),
                dto.getHasNic(),
                dto.getNic()
        );

        if (Boolean.TRUE.equals(dto.getHasNic())) {
            patient.setNic(dto.getNic());
        }else {
            patient.setNic(null);
        }

        if (dto.getEmail() != null && dto.getEmail().isBlank()) {
            patient.setEmail(null);
        }




        Patient savedPatient = patientRepo.save(patient);

        return mapToResponse(savedPatient);
    }

    public List<Patient> searchPatients(String query) {
        Long id = null;

        try {
            id = Long.parseLong(query);
        } catch (Exception ignored) {}

        return patientRepo
                .findByIdOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(
                        id, query, query, query
                );
    }

    public void validatePatient(String birthDate, Boolean hasNic, String nic) {

        if (hasNic == null) {
            throw new RuntimeException("Has NIC selection is required");
        }

        if (birthDate == null || birthDate.isBlank()) return;

        int age = Period.between(
                LocalDate.parse(birthDate),
                LocalDate.now()
        ).getYears();

        // UNDER 16: hasNic must be FALSE
        if (age < 16 && Boolean.TRUE.equals(hasNic)) {
            throw new RuntimeException(
                    "Patients under 16 years old cannot have NIC"
            );
        }

        //UNDER 16: NIC must be empty
        if (age < 16 && nic != null && !nic.isBlank()) {
            throw new RuntimeException(
                    "NIC is not allowed for patients under 16"
            );
        }

        // Adult says NO NIC but still sends NIC
        if (age >= 16 && Boolean.FALSE.equals(hasNic) && nic != null && !nic.isBlank()) {
            throw new RuntimeException(
                    "NIC must be empty when 'Has NIC' is set to No"
            );
        }

        // Adult says YES NIC but NIC missing
        if (age >= 16 && Boolean.TRUE.equals(hasNic)) {
            if (nic == null || nic.isBlank()) {
                throw new RuntimeException(
                        "NIC is required when 'Has NIC' is set to Yes"
                );
            }
        }
    }




    private PatientProfileResponseDto mapToResponse(Patient patient) {

        PatientProfileResponseDto response = new PatientProfileResponseDto();

        response.setPatientId(patient.getId());
        response.setFullName(patient.getFullName());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        response.setDateOfBirth(
                patient.getDateOfBirth() != null
                        ? patient.getDateOfBirth().toString()
                        : null
        );
        response.setAddress(patient.getAddress());

        // Because this patient has NO user
        response.setUserId(null);

        return response;
    }

    private void checkDuplicateChild(
            User user,
            String fullName,
            LocalDate dob,
            String nic,
            String email
    ) {
        //NIC has highest priority
        if (nic != null && !nic.isBlank()) {
            if (patientRepo.findByNic(nic).isPresent()) {
                throw new RuntimeException("Patient already exists with this NIC");
            }
            return;
        }

        //Email has second priority
        if (email != null && !email.isBlank()) {
            if (patientRepo.findByEmail(email).isPresent()) {
                throw new RuntimeException("Patient already exists with this email");
            }
            return;
        }

        //Fallback: Name + DOB + User
        boolean exists = patientRepo
                .findByUserIdAndFullNameIgnoreCaseAndDateOfBirth(
                        user.getId(), fullName, dob
                )
                .isPresent();

        if (exists) {
            throw new RuntimeException(
                    "This family member already exists (same name and date of birth)"
            );
        }
    }


    private void checkDuplicateAdminPatient(
            String fullName,
            LocalDate dob,
            String nic,
            String email
    ) {
        //NIC (global)
        if (nic != null && !nic.isBlank()) {
            if (patientRepo.findByNic(nic).isPresent()) {
                throw new RuntimeException("Patient already exists with this NIC");
            }
            return;
        }

        //Email (global)
        if (email != null && !email.isBlank()) {
            if (patientRepo.findByEmail(email).isPresent()) {
                throw new RuntimeException("Patient already exists with this email");
            }
            return;
        }

        //GLOBAL name + DOB
        boolean exists = patientRepo
                .findByFullNameIgnoreCaseAndDateOfBirth(fullName, dob)
                .isPresent();

        if (exists) {
            throw new RuntimeException(
                    "Patient already exists with same name and date of birth"
            );
        }
    }





}
