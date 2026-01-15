package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.PatientListDto;
import com.dentpulse.dentalsystem.dto.TreatmentRecordDTO;
import com.dentpulse.dentalsystem.entity.DentalService;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.TreatmentRecord;
import com.dentpulse.dentalsystem.repository.DentalServiceRepository;
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
    private DentalServiceRepository dentalServiceRepository;


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

    //For admin use===========================================================
    public TreatmentRecordDTO adminUpdateRecords(TreatmentRecordDTO dto) {

        TreatmentRecord treatment = modelMapper.map(dto, TreatmentRecord.class);

        DentalService dentalService = dentalServiceRepository.findById(dto.getDentalServiceId())
                .orElseThrow(() -> new RuntimeException("Dental service not found"));

        treatment.setDentalService(dentalService);

        treatmentRecordRepo.save(treatment);

        // Fill response fields
        dto.setDentalServiceName(dentalService.getName());
        dto.setDentalServicePrice(dentalService.getPrice());

        return dto;
    }

    public PatientListDto adminGetRecordsByPatientId(Long patientId) {

        Patient patient = patientRepositoryRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        PatientListDto patientDto = modelMapper.map(patient, PatientListDto.class);

        List<TreatmentRecordDTO> treatmentDtos =
                treatmentRecordRepo.findByPatientId(patientId)
                        .stream()
                        .map(treatment -> {
                            TreatmentRecordDTO dto = modelMapper.map(treatment, TreatmentRecordDTO.class);

                            dto.setDentalServiceId(treatment.getDentalService().getId());
                            dto.setDentalServiceName(treatment.getDentalService().getName());
                            dto.setDentalServicePrice(treatment.getDentalService().getPrice());

                            return dto;
                        })
                        .toList();

        patientDto.setTreatmentRecords(treatmentDtos);

        return patientDto;
    }



}
