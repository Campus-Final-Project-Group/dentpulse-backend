package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.TreatmentRecordDTO;
import com.dentpulse.dentalsystem.entity.TreatmentRecord;
import com.dentpulse.dentalsystem.repository.TreatmentRecordRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Transactional
@Service
public class TreatmentRecordService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TreatmentRecordRepository treatmentRecordRepo;
    public TreatmentRecordDTO updateRecords(TreatmentRecordDTO treatmentrecordDTO){
        treatmentRecordRepo.save(modelMapper.map(treatmentrecordDTO, TreatmentRecord.class));
        return treatmentrecordDTO;

    }
}
