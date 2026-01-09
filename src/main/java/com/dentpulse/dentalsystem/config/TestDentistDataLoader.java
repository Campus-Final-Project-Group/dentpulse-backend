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
public class TestDentistDataLoader {

    @Bean
    CommandLineRunner createTestDentist(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.existsByEmail("dentist@gmail.com")) {
                return;
            }

            User user = new User();
            user.setUserName("Dr T.A.Sandalekha");
            user.setEmail("dentist@gmail.com");
            user.setContact("0715466337");
            user.setGender("FEMALE");

            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(Role.DENTIST);

            user.setAuthProvider(AuthProvider.LOCAL);
            user.setActive(true);
            user.setEmailVerified(true);

            userRepository.save(user);

            System.out.println("✅ Test dentist user created");
        };
    }
}
