package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.AppointmentDTO;
import com.dentpulse.dentalsystem.dto.PatientListDto;
import com.dentpulse.dentalsystem.dto.TreatmentRecordDTO;
import com.dentpulse.dentalsystem.service.TreatmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
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
    /*new*/
    @GetMapping("/patient/{patientId}/all")
    public List<TreatmentRecordDTO> getAllRecordsByPatient(@PathVariable Long patientId) {
        return treatmentRecordService.getAllTreatmentRecordsByPatient(patientId);
    }
    /*new*/
    @GetMapping("/all")
    public List<TreatmentRecordDTO> getAllRecords() {
        return treatmentRecordService.getAllTreatmentRecords();
    }

//    @GetMapping
//    public List<TreatmentRecordDTO> getRecords(){
//
//        return TreatmentRecordService.getAllRecords();
//    }

}
