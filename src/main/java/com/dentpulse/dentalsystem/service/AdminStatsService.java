package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.repository.AppointmentRepository;
import com.dentpulse.dentalsystem.repository.BillRepository;
import com.dentpulse.dentalsystem.repository.TreatmentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final TreatmentRecordRepository treatmentRecordRepository;

    // Monthly Revenue
    public List<Map<String,Object>> getMonthlyRevenue(){

        List<Object[]> results = billRepository.getMonthlyRevenue();
        List<Map<String,Object>> data = new ArrayList<>();

        for(Object[] row : results){

            Map<String,Object> map = new HashMap<>();

            map.put("month", row[0]);
            map.put("revenue", row[1]);

            data.add(map);
        }

        return data;
    }

    // Appointment Time Chart
    public List<Map<String,Object>> getAppointmentTimes(){

        List<Object[]> results = appointmentRepository.getAppointmentsByTime();
        List<Map<String,Object>> data = new ArrayList<>();

        for(Object[] row : results){

            Map<String,Object> map = new HashMap<>();

            map.put("time", row[0]);
            map.put("count", row[1]);

            data.add(map);
        }

        return data;
    }

    // Treatment Chart
    public List<Map<String,Object>> getTreatmentStats(){

        List<Object[]> results = treatmentRecordRepository.getTreatmentStats();
        List<Map<String,Object>> data = new ArrayList<>();

        for(Object[] row : results){

            Map<String,Object> map = new HashMap<>();

            map.put("treatment", row[0]);
            map.put("count", row[1]);

            data.add(map);
        }

        return data;
    }

    //Appointment Distribution by Day chart
    public List<Map<String,Object>> getAppointmentsByDay(){

        List<Object[]> results = appointmentRepository.getAppointmentsByDay();
        List<Map<String,Object>> data = new ArrayList<>();

        for(Object[] row : results){

            Map<String,Object> map = new HashMap<>();

            map.put("day", row[0]);
            map.put("count", row[1]);

            data.add(map);
        }

        return data;
    }
}
