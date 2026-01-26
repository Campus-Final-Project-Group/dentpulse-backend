package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Prescription {

    @Id
    private LocalDate date;

    private int count;

    // 🔹 REQUIRED by Hibernate
    public Prescription() {
    }

    // 🔹 Used in service: new Prescription(today, 0)
    public Prescription(LocalDate date, int count) {
        this.date = date;
        this.count = count;
    }

    // 🔹 Getter
    public int getCount() {
        return count;
    }

    // 🔹 Setter
    public void setCount(int count) {
        this.count = count;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
