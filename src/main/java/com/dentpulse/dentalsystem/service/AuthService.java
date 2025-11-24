package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.config.JwtUtil;
import com.dentpulse.dentalsystem.dto.*;
import com.dentpulse.dentalsystem.entity.*;
import com.dentpulse.dentalsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public UserDto registerPatient(RegisterPatientRequest dto) {

        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        User user = new User();
        user.setUserName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setContact(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.PATIENT);

        User savedUser = userRepo.save(user);

        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setDateOfBirth(dto.getBirthDate());
        patientRepo.save(patient);

        UserDto response = new UserDto();
        response.setId(savedUser.getId());
        response.setFullName(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().name());

        return response;
    }

    public LoginResponseDto login(LoginRequest dto) {

        User user = userRepo.findByEmail(dto.getEmail());

        if (user == null) throw new RuntimeException("Email not found!");
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new RuntimeException("Incorrect password!");

        user.updateLastLogin();
        userRepo.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDto(userDto, token);
    }
}
