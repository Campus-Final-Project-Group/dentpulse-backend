package com.dentpulse.dentalsystem.service;

import com.dentpulse.dentalsystem.dto.RequestAdminDto;
import com.dentpulse.dentalsystem.dto.ResponseAdminDto;
import com.dentpulse.dentalsystem.entity.Role;
import com.dentpulse.dentalsystem.entity.User;
import com.dentpulse.dentalsystem.exception.EntryNotFoundException;
import com.dentpulse.dentalsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepo;

    public ResponseAdminDto  createAdmin(RequestAdminDto dto) {
        User admin = new User();
        admin.setUserName(dto.getUserName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword()); // encode later
        admin.setContact(dto.getContactNumber());

        // FORCE ADMIN ROLE
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        admin.setEmailVerified(true);

        User savedAdmin = userRepo.save(admin);

        return toResponseAdminDto(savedAdmin);
    }

    public void updateAdmin(RequestAdminDto dto, Long id) {

        User admin = userRepo.findById(id)
                .orElseThrow(() ->
                        new EntryNotFoundException("Admin not found with ID: " + id));

        // 🔒 Safety check: ensure this user is actually an ADMIN
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not an admin");
        }

        // Update allowed fields
        admin.setUserName(dto.getUserName());
        admin.setEmail(dto.getEmail());
        admin.setContact(dto.getContactNumber());

        userRepo.save(admin);
    }

    public void deleteAdmin(Long id) {

        User admin = userRepo.findById(id)
                .orElseThrow(() ->
                        new EntryNotFoundException("Admin not found with ID: " + id));


        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not an admin");
        }

        //just set active status to false and not delete the data from the table
        admin.setActive(false);
        userRepo.save(admin);
    }




    /* ====================== Mappers ====================== */

    private User toAdminUser(RequestAdminDto dto) {

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // ⚠ encode later
        user.setContact(dto.getContactNumber());
        user.setRole(Role.ADMIN);

        user.setActive(true);
        user.setEmailVerified(true); // admin usually verified by default
        return user;
    }

    private ResponseAdminDto toResponseAdminDto(User user) {
        ResponseAdminDto dto = new ResponseAdminDto();
        dto.setUsername(user.getUserName());
        return dto;
    }



}
