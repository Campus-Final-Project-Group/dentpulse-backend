package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.AppointmentDTO;
import com.dentpulse.dentalsystem.dto.PatientListDto;
import com.dentpulse.dentalsystem.dto.TreatmentRecordDTO;
import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.entity.Patient;
import com.dentpulse.dentalsystem.entity.TreatmentRecord;
import com.dentpulse.dentalsystem.repository.PatientRepository;
import com.dentpulse.dentalsystem.repository.TreatmentRecordRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
//        treatmentRecordRepo.save(modelMapper.map(treatmentrecordDTO, TreatmentRecord.class));
//        return treatmentrecordDTO;


            // Map DTO to entity
            TreatmentRecord record = modelMapper.map(treatmentrecordDTO, TreatmentRecord.class);

            // Fetch patient entity from DB
            Patient patient = patientRepositoryRepo.findById(treatmentrecordDTO.getPatient_id())
                    .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + treatmentrecordDTO.getPatient_id()));

            // Set patient in TreatmentRecord
            record.setPatient(patient);

            // Save record
            treatmentRecordRepo.save(record);

            return treatmentrecordDTO;
        }





    public PatientListDto getRecordsByPatientId(Long patientId) {
        return patientRepositoryRepo.findById(patientId)
                .map(patient -> modelMapper.map(patient, PatientListDto.class))
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
    }

    /*new*/
    public List<TreatmentRecordDTO> getAllTreatmentRecordsByPatient(Long patientId) {
        List<TreatmentRecord> records = treatmentRecordRepo.findByPatientId(patientId);
        return records.stream()
                .map(record -> new TreatmentRecordDTO(
                        record.getTreatment_id(),
                        record.getPatient().getId(),
                        record.getTreatment_date(),
                        record.getDiagnosis(),
                        record.getDentist_note(),
                        record.getTreatmentType().name()
                ))
                .toList();
    }
    /*new*/
    public List<TreatmentRecordDTO> getAllTreatmentRecords() {
        List<TreatmentRecord> records = treatmentRecordRepo.findAll();
        return records.stream()
                .map(record -> new TreatmentRecordDTO(
                        record.getTreatment_id(),
                        record.getPatient().getId(),
                        record.getTreatment_date(),
                        record.getDiagnosis(),
                        record.getDentist_note(),
                        record.getTreatmentType().name()
                ))
                .toList();
    }
    /*new*/
    public Map<String, Long> getTreatmentCharts() {
        List<TreatmentRecord> allRecords = treatmentRecordRepo.findAll();

        return allRecords.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getTreatmentType().name(),
                        Collectors.counting()
                ));
    }








}
