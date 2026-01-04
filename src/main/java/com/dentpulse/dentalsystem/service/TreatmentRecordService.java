package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.PatientListDto;
import com.dentpulse.dentalsystem.dto.TreatmentRecordDTO;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.TreatmentRecord;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.TreatmentRecordRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Transactional
@Service
public class TreatmentRecordService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TreatmentRecordRepository treatmentRecordRepo;

    @Autowired
    private PatientRepository patientRepositoryRepo;
    public TreatmentRecordDTO updateRecords(TreatmentRecordDTO treatmentrecordDTO){
        treatmentRecordRepo.save(modelMapper.map(treatmentrecordDTO, TreatmentRecord.class));
        return treatmentrecordDTO;

    }


    public PatientListDto getRecordsByPatientId(Long patientId) {
        return patientRepositoryRepo.findById(patientId)
                .map(patient -> modelMapper.map(patient, PatientListDto.class))
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
    }



}
