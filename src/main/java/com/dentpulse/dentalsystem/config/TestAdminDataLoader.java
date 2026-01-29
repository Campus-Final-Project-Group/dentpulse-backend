package com.dentpulse.dentalsystem.config;

import com.dentpulse.dentalsystem.entity.AuthProvider;
import com.dentpulse.dentalsystem.entity.Role;
import com.dentpulse.dentalsystem.entity.User;
import com.dentpulse.dentalsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class TestAdminDataLoader {
    @Bean
    CommandLineRunner createTestAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.existsByEmail("admin@gmail.com")) {
                return;
            }

            User user = new User();
            user.setUserName("Admin");
            user.setEmail("admin@gmail.com");
            user.setContact("0771234567");
            user.setGender("MALE");

            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRole(Role.ADMIN);

            user.setAuthProvider(AuthProvider.LOCAL);
            user.setActive(true);
            user.setEmailVerified(true);

            userRepository.save(user);

            System.out.println("✅ Test admin user created");
        };
    }
}
