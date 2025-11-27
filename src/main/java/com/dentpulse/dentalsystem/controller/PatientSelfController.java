package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.PatientProfileDto;
import com.dentpulse.dentalsystem.dto.UpdatePatientRequest;
import com.dentpulse.dentalsystem.service.PatientSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
