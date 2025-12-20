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
import java.util.Map;


@RestController

@RequestMapping(value = "/api/appointments")
@CrossOrigin(origins = "http://localhost:3000")

public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;





    @GetMapping
    public List<AppointmentDTO> getAppointments(){

        return appointmentService.getAllAppointments();
    }

    @GetMapping("/status")
    public Map<String, Long> getAppointmentStats() {
        return appointmentService.getAppointmentStats();
    }




}
