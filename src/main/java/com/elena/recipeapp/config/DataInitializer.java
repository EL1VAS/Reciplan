package com.elena.recipeapp.config;

import com.elena.recipeapp.user.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.elena.recipeapp.enums.Roles;
import com.elena.recipeapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if(userRepository.count() == 0) {

            User admin = User.builder()
                    .username("admin")
                    .email("admin@recipeapp.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Roles.ADMIN)
                    .build();

            User user = User.builder()
                    .username("elena")
                    .email("elena@recipeapp.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(Roles.USER)
                    .build();

            userRepository.save(admin);
            userRepository.save(user);

            log.info("Test data initialized: admin + elena");
        } else {
            log.info("Database already has data, skipping initialization");
        }
    }
    
}
