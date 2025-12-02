package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.TreatmentRecordDTO;
import com.dentpulse.dentalsystem.service.TreatmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/records")
public class TreatmentRecordController {

    @Autowired
    private TreatmentRecordService treatmentRecordService;
    @PostMapping
    public TreatmentRecordDTO saveTreatmentrecord(@RequestBody TreatmentRecordDTO treatmentRecordDTO){
        return treatmentRecordService.updateRecords(treatmentRecordDTO);
    }

}
