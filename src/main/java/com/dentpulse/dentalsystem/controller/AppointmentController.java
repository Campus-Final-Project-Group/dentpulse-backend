package com.dentpulse.dentalsystem.controller;

import com.dentpulse.dentalsystem.dto.AppointmentDTO;
import com.dentpulse.dentalsystem.service.AppointmentService;
import com.dentpulse.dentalsystem.service.TreatmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/appointments")

public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;





    @GetMapping
    public List<AppointmentDTO> getAppointments(){

        return appointmentService.getAllAppointments();
    }



}
