package com.dentpulse.dentalsystem.repository;

import com.dentpulse.dentalsystem.entity.Role;
import com.dentpulse.dentalsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);

    List<User> findByRole(Role role);
}
