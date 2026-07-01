package com.elena.recipeapp.config;

import com.elena.recipeapp.user.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.elena.recipeapp.category.model.Category;
import com.elena.recipeapp.category.repository.CategoryRepository;
import com.elena.recipeapp.common.exceptions.ResourceNotFoundException;

import com.elena.recipeapp.enums.Roles;
import com.elena.recipeapp.ingredient.model.Ingredient;
import com.elena.recipeapp.ingredient.repository.IngredientRepository;
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
    private final IngredientRepository ingredientRepository;

    @Value("${app.init.admin-password}")
    private String adminPassword;

    @Value("${app.init.user-password}")
    private String userPassword;

    @Override
    public void run(String... args) {
        seedUsers();
        seedCategories();
        seedIngredients();
    }

   // Helper methods

   private Category category (String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + name));
    }

    private Ingredient ingredient(String name) {
        return ingredientRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found: " + name));
    }



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

        if (!categoryRepository.existsByNameIgnoreCase("Meats / Protein")) {
            Category meatsProtein = Category.builder()
                    .name("Meats / Protein")
                    .build();

            categoryRepository.save(meatsProtein);
        }

        if (!categoryRepository.existsByNameIgnoreCase("Produce")) {
            Category produce = Category.builder()
                    .name("Produce")
                    .build();

            categoryRepository.save(produce);
        }

        if (!categoryRepository.existsByNameIgnoreCase("Grains / Pasta / Rice")) {
            Category grainsPastaRice = Category.builder()
                    .name("Grains / Pasta / Rice")
                    .build();

            categoryRepository.save(grainsPastaRice);
        }

        if (!categoryRepository.existsByNameIgnoreCase("Canned / Jarred")) {
            Category cannedAndJarred = Category.builder()
                    .name("Canned / Jarred")
                    .build();

            categoryRepository.save(cannedAndJarred);
        }

        if (!categoryRepository.existsByNameIgnoreCase("Spices / Herbs")) {
            Category spicesHerbs = Category.builder()
                    .name("Spices / Herbs")
                    .build();

            categoryRepository.save(spicesHerbs);
        }

        if (!categoryRepository.existsByNameIgnoreCase("Bread / Bakery")) {
            Category breadBakery = Category.builder()
                    .name("Bread / Bakery")
                    .build();

            categoryRepository.save(breadBakery);
        }
    }

    private void seedIngredients() {

        if (!ingredientRepository.existsByNameIgnoreCase("Milk low fat")) {
            Ingredient milkLowFat = Ingredient.builder()
                    .name("Milk low fat")
                    .category(categoryRepository.findByNameIgnoreCase("Dairy & Eggs").orElseThrow())
                    .build();
            ingredientRepository.save(milkLowFat);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Milk full fat")) {
            Ingredient milkFullFat = Ingredient.builder()
                    .name("Milk full fat")
                    .category(categoryRepository.findByNameIgnoreCase("Dairy & Eggs").orElseThrow())
                    .build();
            ingredientRepository.save(milkFullFat);
        }
        
        if (!ingredientRepository.existsByNameIgnoreCase("Eggs")) {
            Ingredient eggs = Ingredient.builder()
                    .name("Eggs")
                    .category(categoryRepository.findByNameIgnoreCase("Dairy & Eggs").orElseThrow())
                    .build();
            ingredientRepository.save(eggs);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Parmesan")) {
            Ingredient parmesan = Ingredient.builder()
                    .name("Parmesan")
                    .category(categoryRepository.findByNameIgnoreCase("Dairy & Eggs").orElseThrow())
                    .build();
            ingredientRepository.save(parmesan);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Chicken breast")) {
            Ingredient chickenBreast = Ingredient.builder()
                    .name("Chicken breast")
                    .category(categoryRepository.findByNameIgnoreCase("Meats / Protein").orElseThrow())
                    .build();
            ingredientRepository.save(chickenBreast);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Tuna")) {
            Ingredient tuna = Ingredient.builder()
                    .name("Tuna")
                    .category(categoryRepository.findByNameIgnoreCase("Meats / Protein").orElseThrow())
                    .build();
            ingredientRepository.save(tuna);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Onion")) {
            Ingredient onion = Ingredient.builder()
                    .name("Onion")
                    .category(categoryRepository.findByNameIgnoreCase("Produce").orElseThrow())
                    .build();
            ingredientRepository.save(onion);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Garlic")) {
            Ingredient garlic = Ingredient.builder()
                    .name("Garlic")
                    .category(categoryRepository.findByNameIgnoreCase("Produce").orElseThrow())
                    .build();
            ingredientRepository.save(garlic);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Tomato")) {
            Ingredient tomato = Ingredient.builder()
                    .name("Tomato")
                    .category(categoryRepository.findByNameIgnoreCase("Produce").orElseThrow())
                    .build();
            ingredientRepository.save(tomato);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Spinach")) {
            Ingredient spinach = Ingredient.builder()
                    .name("Spinach")
                    .category(categoryRepository.findByNameIgnoreCase("Produce").orElseThrow())
                    .build();
            ingredientRepository.save(spinach);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Potato")) {
            Ingredient potato = Ingredient.builder()
                    .name("Potato")
                    .category(categoryRepository.findByNameIgnoreCase("Produce").orElseThrow())
                    .build();
            ingredientRepository.save(potato);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Oregano")) {
            Ingredient oregano = Ingredient.builder()
                    .name("Oregano")
                    .category(categoryRepository.findByNameIgnoreCase("Spices / Herbs").orElseThrow())
                    .build();
            ingredientRepository.save(oregano);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("BlackPepper")) {
            Ingredient blackPepper = Ingredient.builder()
                    .name("Black Pepper")
                    .category(categoryRepository.findByNameIgnoreCase("Spices / Herbs").orElseThrow())
                    .build();
            ingredientRepository.save(blackPepper);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Salt")) {
            Ingredient salt = Ingredient.builder()
                    .name("Salt")
                    .category(categoryRepository.findByNameIgnoreCase("Spices / Herbs").orElseThrow())
                    .build();
            ingredientRepository.save(salt);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Paprika")) {
            Ingredient paprika = Ingredient.builder()
                    .name("Paprika")
                    .category(categoryRepository.findByNameIgnoreCase("Spices / Herbs").orElseThrow())
                    .build();
            ingredientRepository.save(paprika);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Pasta")) {
            Ingredient pasta = Ingredient.builder()
                    .name("Pasta")
                    .category(categoryRepository.findByNameIgnoreCase("Grains / Pasta / Rice").orElseThrow())
                    .build();
            ingredientRepository.save(pasta);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Rice")) {
            Ingredient rice = Ingredient.builder()
                    .name("Rice")
                    .category(categoryRepository.findByNameIgnoreCase("Grains / Pasta / Rice").orElseThrow())
                    .build();
            ingredientRepository.save(rice);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Bread")) {
            Ingredient bread = Ingredient.builder()
                    .name("Bread")
                    .category(categoryRepository.findByNameIgnoreCase("Bread / Bakery").orElseThrow())
                    .build();
            ingredientRepository.save(bread);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Tomato Sauce")) {
            Ingredient tomatoSauce = Ingredient.builder()
                    .name("Tomato Sauce")
                    .category(categoryRepository.findByNameIgnoreCase("Canned / Jarred").orElseThrow())
                    .build();
            ingredientRepository.save(tomatoSauce);
        }

        if (!ingredientRepository.existsByNameIgnoreCase("Chickpeas")) {
            Ingredient chickpeas = Ingredient.builder()
                    .name("Chickpeas")
                    .category(categoryRepository.findByNameIgnoreCase("Canned / Jarred").orElseThrow())
                    .build();
            ingredientRepository.save(chickpeas);
        }
    }   



        



    

}



