package com.dentpulse.dentalsystem.service;

import org.springframework.stereotype.Service;

@Service
public class ClinicInfoService {

    public String getClinicLocation() {
        return """
        📍 Clinic Location

        DentPulse Dental Clinic
        No. 05, Nagoda Junction,
        Kalutara, Sri Lanka
        """;
    }

    public String getDoctors() {
        return """
        👨‍⚕️ Our Doctor

        Dr.T.A.Sandalekha
        (Dentist)
        """;
    }

    public String getOpeningHours() {
        return """
        ⏰ Opening Hours

        Monday – Friday
        4:00 PM – 7:30 PM

        Saturday & Sunday
        10:30 AM – 4:30 PM

        📞 Contact: +94 71 546 6337
        """;
    }

    public String getServices() {
        return """
        🦷 Our Dental Services

        • Tooth Extraction
        • Tooth Filling
        • Root Canal Treatment
        • Teeth Cleaning (Scaling)
        • Surgical Dental Procedures
        """;
    }
}