package com.dentpulse.dentalsystem.service;


import com.dentpulse.dentalsystem.dto.AppointmentDTO;
import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.entity.AppointmentStatus;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;


import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepo;



    @Autowired
    private ModelMapper modelMapper;

    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointmentList = appointmentRepo.findAll();
        return modelMapper.map(appointmentList, new TypeToken<List<AppointmentDTO>>() {
                }.getType()
        );
    }


    public Map<String, Long> getAppointmentStats() {

        Map<String, Long> status = new HashMap<>();

        status.put("total", appointmentRepo.count());
        status.put("scheduled", appointmentRepo.countByStatus(AppointmentStatus.SCHEDULED));
        status.put("completed", appointmentRepo.countByStatus(AppointmentStatus.COMPLETED));
        status.put("cancelled", appointmentRepo.countByStatus(AppointmentStatus.CANCELLED));

        return status;
    }


/*    public List<AppointmentDTO> getTodayAppointments() {
        LocalDate today = LocalDate.now();

        // Convert LocalDate → java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(today);

        // Convert java.sql.Date → java.util.Date  (MATCH ENTITY)
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

        List<Appointment> appointments =
                appointmentRepo.findByAppointment_dateOrderByStartTimeAsc(utilDate);

        return modelMapper.map(appointments,
                new TypeToken<List<AppointmentDTO>>(){}.getType());
    }*/





}
