package com.dentpulse.dentalsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(
        name = "patient",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"full_name", "date_of_birth"})
})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @Column(length = 255)
    private String address;

    @Column(name = "full_name", length = 100)
    private String fullName;

    //Add unique=true and nullable=true
    @Column(name = "email", unique = true, nullable = true)
    private String email;

    @Column(name = "phone",  nullable = true)
    private String phone;

    @Column(name = "relationship", length = 50)
    private String relationship;

    @Column(name = "gender", length = 10)
    private String gender;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();


    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "is_account_owner", nullable = false)
    private boolean accountOwner;


    @OneToMany(mappedBy = "patient")
    private List<TreatmentRecord> treatment_record;

    //Add new field NIC
    @Size(min = 10, max = 12, message = "NIC must be between 10 and 12 characters")
    @Column(name = "nic", unique = true, nullable = true)
    private String nic;

}