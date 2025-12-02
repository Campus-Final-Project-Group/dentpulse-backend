package com.dentpulse.dentalsystem.service;


import com.dentpulse.dentalsystem.dto.AppointmentDTO;
import com.dentpulse.dentalsystem.entity.Appointment;
import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;


import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

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
