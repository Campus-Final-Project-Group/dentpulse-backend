package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.TreatmentRecordDTO;
import com.dentpulse.dentalsystem.service.TreatmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
