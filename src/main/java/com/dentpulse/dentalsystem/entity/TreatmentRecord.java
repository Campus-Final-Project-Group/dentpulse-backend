package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

@Table(name = "Treatment_Records")

public class TreatmentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private int treatment_id;

    @ManyToOne
    @JoinColumn(name = "patient_id")   // column name in appointment table
    private Patient patient;


    @Column(name = "treatment_date")
    private Date treatment_date;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "dentist_note")
    private String dentist_note;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] attachment;



}

