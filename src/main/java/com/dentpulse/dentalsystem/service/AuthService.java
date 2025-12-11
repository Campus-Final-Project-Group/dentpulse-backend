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

    @Autowired
    private EmailService emailService;


    private String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000; // 100000 - 999999
        return String.valueOf(otp);
    }



    // REGISTER PATIENT
    public UserDto registerPatient(RegisterPatientRequest dto) {

        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        // Save USER
        User user = new User();
        user.setUserName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setContact(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.PATIENT);

        //  OTP PART
        String otp = generateOtp();
        user.setEmailVerified(false);
        user.setOtpCode(otp);
        user.setOtpExpiresAt(java.time.LocalDateTime.now().plusMinutes(10));

        User savedUser = userRepo.save(user);

        // Save PATIENT
        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setDateOfBirth(dto.getBirthDate());
        patient.setAddress(dto.getAddress());   // 🔥 IMPORTANT
        patientRepo.save(patient);

        //  Send OTP email
        emailService.sendOtpEmail(savedUser.getEmail(), otp);

        // Response DTO
        UserDto response = new UserDto();
        response.setId(savedUser.getId());
        response.setFullName(savedUser.getUserName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().name());

        return response;
    }

    //verifyEmail
    public void verifyEmail(VerifyEmailRequest request) {
        User user = userRepo.findByEmail(request.getEmail());

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified!");
        }

        if (user.getOtpCode() == null || user.getOtpExpiresAt() == null) {
            throw new RuntimeException("OTP not generated");
        }

        if (java.time.LocalDateTime.now().isAfter(user.getOtpExpiresAt())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!user.getOtpCode().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP code");
        }

        //  Mark as verified
        user.setEmailVerified(true);
        user.setOtpCode(null);
        user.setOtpExpiresAt(null);

        userRepo.save(user);
    }


    // LOGIN
    public LoginResponseDto login(LoginRequest dto) {

        User user = userRepo.findByEmail(dto.getEmail());

        if (user == null) throw new RuntimeException("Email not found!");
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new RuntimeException("Incorrect password!");


        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in.");
        }

        user.updateLastLogin();
        userRepo.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDto(userDto, token);
    }
}
