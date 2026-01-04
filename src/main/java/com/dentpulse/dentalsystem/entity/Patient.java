package com.dentpulse.dentalsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @Column(length = 255)
    private String address;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "relationship", length = 50)
    private String relationship;

    @Column(name = "gender", length = 10)
    private String gender;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();


    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "is_account_owner", nullable = false)
    private boolean accountOwner;


    @OneToMany(mappedBy = "patient")
    private List<TreatmentRecord> treatment_record;
}
