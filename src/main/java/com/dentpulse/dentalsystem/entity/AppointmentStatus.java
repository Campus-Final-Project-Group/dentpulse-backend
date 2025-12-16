package com.dentpulse.dentalsystem.entity;

/**
 * Appointment status for patient booking flow.
 */
public enum AppointmentStatus {
    PENDING,     // booked by patient, waiting
    CONFIRMED,   // later dentist/admin can confirm
    COMPLETED,   // finished
    CANCELLED    // cancelled (slot becomes free again)
}
