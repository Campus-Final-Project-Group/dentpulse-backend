package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.*;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.service.PatientSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
@CrossOrigin
public class PatientSelfController {

    @Autowired
    private PatientSelfService patientService;

    //  Get logged-in user's profile (ACCOUNT OWNER ONLY)
    @GetMapping("/me")
    public ResponseEntity<PatientProfileDto> getProfile(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(patientService.getMyProfile(token.substring(7)));
    }

    // Update profile (email NOT allowed here)
    @PutMapping("/update")
    public ResponseEntity<PatientProfileDto> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdatePatientRequest request) {

        return ResponseEntity.ok(patientService.updateProfile(token.substring(7), request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PatientListDto>> getMyPatients(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                patientService.getMyPatients(token.substring(7))
        );
    }

    @GetMapping("/family")
    public ResponseEntity<List<FamilyMemberDto>> getMyFamilyMembers(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                patientService.getMyFamilyMembers(token.substring(7))
        );
    }

    @GetMapping("/mefortable")
    public ResponseEntity<PatientProfileForTableDto> getProfileForTable(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(patientService.getMyProfileFortabel(token.substring(7)));
    }


    @PostMapping("/family")
    public ResponseEntity<?> addFamilyMember(
            @RequestHeader("Authorization") String token,
            @RequestBody AddFamilyMemberRequest request) {

        patientService.addFamilyMember(token.substring(7), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/family/{patientId}")
    public ResponseEntity<?> deleteFamilyMember(
            @RequestHeader("Authorization") String token,
            @PathVariable Long patientId) {

        patientService.deleteFamilyMember(token.substring(7), patientId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/family/{patientId}")
    public ResponseEntity<?> updateFamilyMember(
            @RequestHeader("Authorization") String token,
            @PathVariable Long patientId,
            @RequestBody UpdateFamilyMemberRequest request) {

        patientService.updateFamilyMember(token.substring(7), patientId, request);
        return ResponseEntity.ok().build();
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientProfileResponseDto> addPatient(
            @RequestBody PatientProfileDto dto) {

        return ResponseEntity.ok(patientService.createPatientByAdmin(dto));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> searchPatients(@RequestParam String query) {
        return patientService.searchPatients(query);
    }

    @GetMapping("/list-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminPatientTableDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminPatientProfileDto getPatientById(@PathVariable Long id){
        return patientService.getPatientById(id);
    }

    @GetMapping("/admin/{id}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TreatmentRecordDTO> getPatientHistory(@PathVariable Long id) {
        return patientService.getPatientTreatmentHistory(id);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/{id}")
    public PatientIdDto getPatientDetailsById(@PathVariable Long id){
        return patientService.getPatientDetailsById(id);
    }

}
