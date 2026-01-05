package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.config.JwtUtil;
import com.dentpulse.dentalsystem.dto.*;
import com.dentpulse.dentalsystem.entity.*;
import com.dentpulse.dentalsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;


    private String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000; // 100000 - 999999
        return String.valueOf(otp);
    }



    // REGISTER PATIENT
    public UserDto registerPatient(RegisterPatientRequest dto) {

//        if (userRepo.existsByEmail(dto.getEmail())) {
//            throw new RuntimeException("Email already exists!");
//        }

        User existing = userRepo.findByEmail(dto.getEmail());

        if (existing != null && existing.isEmailVerified()) {
            throw new RuntimeException("Email already exists!");
        }


        // Save USER
//        User user = new User();
//        user.setUserName(dto.getFullName());
//        user.setEmail(dto.getEmail());
//        user.setContact(dto.getPhone());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setRole(Role.PATIENT);
//        user.setGender(dto.getGender());

        User user;

        if (existing != null && !existing.isEmailVerified()) {
            user = existing;

            // Optional: update details if user re-registers
            user.setUserName(dto.getFullName());
            user.setContact(dto.getPhone());
            user.setGender(dto.getGender());
            user.setRole(Role.PATIENT);

            // Update password too (optional)
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

        } else {
            // New user
            user = new User();
            user.setUserName(dto.getFullName());
            user.setEmail(dto.getEmail());
            user.setContact(dto.getPhone());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRole(Role.PATIENT);
            user.setGender(dto.getGender());
        }



        //  OTP PART
        String otp = generateOtp();
        user.setEmailVerified(false);
        user.setOtpCode(otp);
        user.setOtpExpiresAt(java.time.LocalDateTime.now().plusMinutes(10));

        User savedUser = userRepo.save(user);

        //  Send OTP email
        emailService.sendOtpEmail(savedUser.getEmail(), otp);

        // Save PATIENT
//        Patient patient = new Patient();
//        patient.setUser(savedUser);

        Patient patient = patientRepo.findByUserIdAndAccountOwnerTrue(savedUser.getId());

        if (patient == null) {
            patient = new Patient();
            patient.setUser(savedUser);
            patient.setAccountOwner(true);
        }

        //  copy identity data ONCE
        patient.setFullName(savedUser.getUserName());
        patient.setEmail(savedUser.getEmail());
        patient.setPhone(savedUser.getContact());

        // profile data
        patient.setDateOfBirth(dto.getBirthDate());
        patient.setAddress(dto.getAddress());
        patient.setGender(savedUser.getGender());

        // mark as account owner
        patient.setAccountOwner(true);

        patientRepo.save(patient);



        // Response DTO
        UserDto response = new UserDto();
        response.setId(savedUser.getId());
        response.setFullName(savedUser.getUserName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().name());
        response.setGender(savedUser.getGender());


        return response;
    }

    //verifyEmail
    public String verifyEmail(VerifyEmailRequest request) {
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

        return "Email verified successfully.";
    }


    public void resendVerifyOtp(String email) {
        User user = userRepo.findByEmail(email);

        if (user == null) throw new RuntimeException("User not found!");
        if (user.isEmailVerified()) throw new RuntimeException("Email already verified!");

        String otp = generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(10));

        userRepo.save(user);
        emailService.sendOtpEmail(user.getEmail(), otp);
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
        userDto.setGender(user.getGender());

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDto(userDto, token);
    }

    public void sendForgotPasswordOtp(String email) {

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found with this email");
        }

        // Generate 6-digit OTP
        String otp = generateOtp();
        // IMPORTANT
        user.setForgotPasswordVerified(false);

        user.setOtpCode(otp);
        user.setOtpExpiresAt(java.time.LocalDateTime.now().plusMinutes(10));


        userRepo.save(user);

        //  reuse existing email service
        emailService.sendOtpEmail(user.getEmail(), otp);
    }


    public void verifyForgotPasswordOtp(String email, String otp) {

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getOtpCode() == null || user.getOtpExpiresAt() == null) {
            throw new RuntimeException("OTP not generated");
        }

        // check expiry
        if (java.time.LocalDateTime.now().isAfter(user.getOtpExpiresAt())) {
            throw new RuntimeException("OTP expired");
        }

        // check OTP match
        if (!user.getOtpCode().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // OTP verified successfully
        user.setForgotPasswordVerified(true);

        // IMPORTANT: mark OTP as used (one-time)
        user.setOtpCode(null);
        user.setOtpExpiresAt(null);

        userRepo.save(user);
    }


    public void resetPassword(String email, String newPassword) {

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        //  MAIN SECURITY CHECK
        if (!Boolean.TRUE.equals(user.getForgotPasswordVerified())) {
            throw new RuntimeException("OTP verification required");
        }

        // Encode new password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Safety: make sure OTP is already cleared
        user.setOtpCode(null);
        user.setOtpExpiresAt(null);

        //  RESET THE FLAG (ONE-TIME USE)
        user.setForgotPasswordVerified(false);

        userRepo.save(user);
    }

    public LoginResponseDto googleLogin(String idToken) {

        // 1. Verify Google token
        var payload = googleTokenVerifier.verify(idToken);

        // 2. Extract safe fields
        String googleId = googleTokenVerifier.getGoogleId(payload);
        String email = googleTokenVerifier.getEmail(payload);
        String name = googleTokenVerifier.getName(payload);
        //String gender = (String) payload.get("gender");
        String gender = googleTokenVerifier.getGender(payload);

        // Validate gender to be either "male" or "female"
        if (gender == null || (!gender.equals("male") && !gender.equals("female"))) {
            throw new RuntimeException("Invalid gender provided. Only 'male' or 'female' are allowed.");
        }

        if (!googleTokenVerifier.isEmailVerified(payload)) {
            throw new RuntimeException("Google email not verified");
        }

        // 3. Check if user exists
        User user = userRepo.findByEmail(email);

        if (user == null) {
            //  REGISTER NEW GOOGLE USER

            user = new User();
            user.setUserName(name);
            user.setEmail(email);
            user.setContact("GOOGLE");
            user.setPassword("GOOGLE_AUTH"); // dummy
            user.setRole(Role.PATIENT);
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setEmailVerified(true);
            user.setGender(gender);

            user = userRepo.save(user);

            // Create patient profile
            Patient patient = new Patient();
            patient.setUser(user);
            patient.setFullName(name);
            patient.setEmail(email);
            patient.setAccountOwner(true);
            patient.setGender(gender);

            patientRepo.save(patient);
        }

        //  Prevent Google user logging in via password
        if (user.getAuthProvider() != AuthProvider.GOOGLE) {
            throw new RuntimeException("Please login using email & password");
        }

        // 4. Update last login
        user.updateLastLogin();
        userRepo.save(user);

        // 5. Generate JWT
        String token = jwtUtil.generateToken(user.getEmail());

        // 6. Response
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setGender(user.getGender());

        return new LoginResponseDto(userDto, token);
    }


}
