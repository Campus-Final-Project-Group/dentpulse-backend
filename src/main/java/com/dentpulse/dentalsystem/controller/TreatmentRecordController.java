package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.PatientListDto;
import com.dentpulse.dentalsystem.dto.TreatmentRecordDTO;
import com.dentpulse.dentalsystem.service.TreatmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping(value = "/api/records")
@CrossOrigin(origins = "http://localhost:3000")

public class TreatmentRecordController {

    @Autowired
    private TreatmentRecordService treatmentRecordService;

    @PostMapping
    public TreatmentRecordDTO saveTreatmentrecord(@RequestBody TreatmentRecordDTO treatmentRecordDTO){
        return treatmentRecordService.updateRecords(treatmentRecordDTO);
    }

    @GetMapping("/patient/{patientId}")
    public PatientListDto getRecordsByPatient(
            @PathVariable Long patientId) {

        return treatmentRecordService.getRecordsByPatientId(patientId);
    }

    //ADMIN ONLY
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public TreatmentRecordDTO adminSaveTreatment(@RequestBody TreatmentRecordDTO dto){
        return treatmentRecordService.adminUpdateRecords(dto);
    }

    //ADMIN ONLY
    @GetMapping("/admin/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public PatientListDto adminGetRecordsByPatient(@PathVariable Long patientId){
        return treatmentRecordService.adminGetRecordsByPatientId(patientId);
    }


}
