package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.*;
import com.dentpulse.dentalsystem.service.PatientSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
@CrossOrigin
public class PatientSelfController {

    @Autowired
    private PatientSelfService patientService;

    @GetMapping("/me")
    public ResponseEntity<PatientProfileDto> getProfile(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(patientService.getMyProfile(token.substring(7)));
    }

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

    @PostMapping("/family")
    public ResponseEntity<?> addFamilyMember(
            @RequestHeader("Authorization") String token,
            @RequestBody AddFamilyMemberRequest request) {

        patientService.addFamilyMember(token.substring(7), request);
        return ResponseEntity.ok().build();
    }



}
