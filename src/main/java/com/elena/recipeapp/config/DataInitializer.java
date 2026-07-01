package com.elena.recipeapp.config;

import com.elena.recipeapp.user.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.elena.recipeapp.category.model.Category;
import com.elena.recipeapp.category.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    @Value("${app.init.admin-password}")
    private String adminPassword;

    @Value("${app.init.user-password}")
    private String userPassword;

    @Override
    public void run(String... args) {
        seedUsers();

        
    }

// Helper methods

private void seedUsers() {
    if(!userRepository.existsByEmail("admin@recipeapp.com") && !userRepository.existsByEmail("elena@recipeapp.com")) {

            User admin = User.builder()
                    .username("admin")
                    .email("admin@recipeapp.com")
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Roles.ADMIN)
                    .build();

            User user = User.builder()
                    .username("elena")
                    .email("elena@recipeapp.com")
                    .password(passwordEncoder.encode(userPassword))
                    .role(Roles.USER)
                    .build();

            userRepository.save(admin);
            userRepository.save(user);

            log.info("Test data initialized: admin + elena");
        } else {
            log.info("Database already has data, skipping initialization");
        }
    }

private  void seedCategories() {
    if (!categoryRepository.existsByNameIgnoreCase("Dairy & Eggs")) {
        Category dairyAndEggs = Category.builder()
                .name("Dairy & Eggs")
                .build();

        categoryRepository.save(dairyAndEggs);
    }

    if (!categoryRepository.existsByNameIgnoreCase("Meat & Seafood")) {
        Category meatAndSeafood = Category.builder()
                .name("Meat & Seafood")
                .build();

        categoryRepository.save(meatAndSeafood);
    }
}



