package com.dentpulse.dentalsystem.entity;


public enum AppointmentStatus {
    // patient side
    PENDING,        // booked by patient, waiting for confirmation

    // admin/doctor side
    CONFIRMED,      // approved by dentist/admin
    SCHEDULED,      // time slot assigned

    // outcome
    COMPLETED,      // appointment finished
    CANCELLED,      // cancelled by patient or admin
    NO_SHOW         // patient did not come
}

