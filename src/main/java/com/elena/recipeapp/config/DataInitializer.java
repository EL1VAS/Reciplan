package com.elena.recipeapp.config;

import com.elena.recipeapp.user.model.User;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.elena.recipeapp.category.model.Category;
import com.elena.recipeapp.category.repository.CategoryRepository;
import com.elena.recipeapp.common.exceptions.ResourceNotFoundException;
import com.elena.recipeapp.enums.Difficulty;
import com.elena.recipeapp.enums.Roles;
import com.elena.recipeapp.enums.Unit;
import com.elena.recipeapp.ingredient.model.Ingredient;
import com.elena.recipeapp.ingredient.repository.IngredientRepository;
import com.elena.recipeapp.recipe.model.Recipe;
import com.elena.recipeapp.recipe.model.RecipeIngredients;
import com.elena.recipeapp.recipe.repository.RecipeRepository;
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
    private final RecipeRepository recipeRepository;

    @Value("${app.init.admin-password}")
    private String adminPassword;

    @Value("${app.init.user-password}")
    private String userPassword;

    @Override
    public void run(String... args) {
        seedUsers();
        seedCategories();
        seedIngredients();
        seedRecipes();
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

    private User user(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    private RecipeIngredients line (String ingredientName, String quantity, Unit unit) {
        return RecipeIngredients.builder()
                .ingredient(ingredient(ingredientName))
                .quantity(new BigDecimal(quantity))
                .unit(unit)
                .build();
    }

    private void createUserIfNotExists(String username, String email, String rawPassword, Roles role) {
        if (!userRepository.existsByEmail(email)) {
            User user = User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword))
                    .role(role)
                    .build();
            userRepository.save(user);
        }
    }

    private void createCategoryIfNotExists(String name) {
        if (!categoryRepository.existsByNameIgnoreCase(name)) {
            Category category = Category.builder()
                    .name(name)
                    .build();
            categoryRepository.save(category);
        }
    }

    private void createIngredientIfNotExists(String name, String categoryName) {
        if (!ingredientRepository.existsByNameIgnoreCase(name)) {
            Ingredient ingredient = Ingredient.builder()
                    .name(name)
                    .category(category(categoryName))
                    .build();
            ingredientRepository.save(ingredient);
        }
    }

    private void createRecipeIfNotExists(
                            String title, 
                            String instructions, 
                            Integer prepTimeMinutes, 
                            Integer cookTimeMinutes, 
                            Difficulty difficulty, 
                            boolean vegetarian, 
                            boolean lactoseFree, 
                            boolean glutenFree,  
                            String createdByEmail, 
                            List<RecipeIngredients> lines) {
        if (!recipeRepository.existsByTitleIgnoreCase(title)) {
            Recipe recipe = Recipe.builder()
                    .title(title)
                    .instructions(instructions)
                    .prepTimeMinutes(prepTimeMinutes)
                    .cookTimeMinutes(cookTimeMinutes)
                    .difficulty(difficulty)
                    .vegetarian(vegetarian)
                    .lactoseFree(lactoseFree)
                    .glutenFree(glutenFree)
                    .createdBy(user(createdByEmail))
                    .build();
            
            lines.forEach(line -> line.setRecipe(recipe));
            recipe.setRecipeIngredients(lines);
            recipeRepository.save(recipe);
        }
    }



    private void seedUsers() {
        createUserIfNotExists("admin", "admin@recipeapp.com", adminPassword, Roles.ADMIN);
        createUserIfNotExists("user", "user@recipeapp.com", userPassword, Roles.USER);

        log.info("User seed completed.");                   
    }

    private  void seedCategories() {
        createCategoryIfNotExists("Dairy & Eggs");
        createCategoryIfNotExists("Meats / Protein");
        createCategoryIfNotExists("Produce");
        createCategoryIfNotExists("Grains / Pasta / Rice");
        createCategoryIfNotExists("Canned / Jarred");
        createCategoryIfNotExists("Spices / Herbs");
        createCategoryIfNotExists("Bread / Bakery");
        createCategoryIfNotExists("Oils / Vinegars");
    }

    private void seedIngredients() {

        createIngredientIfNotExists("Milk low fat", "Dairy & Eggs");
        createIngredientIfNotExists("Milk full fat", "Dairy & Eggs");
        createIngredientIfNotExists("Eggs", "Dairy & Eggs");
        createIngredientIfNotExists("Parmesan", "Dairy & Eggs");
        createIngredientIfNotExists("Chicken breast", "Meats / Protein");
        createIngredientIfNotExists("Tuna", "Meats / Protein");
        createIngredientIfNotExists("Onion", "Produce");
        createIngredientIfNotExists("Garlic", "Produce");
        createIngredientIfNotExists("Tomato", "Produce");
        createIngredientIfNotExists("Spinach", "Produce");
        createIngredientIfNotExists("Potato", "Produce");
        createIngredientIfNotExists("Oregano", "Spices / Herbs");
        createIngredientIfNotExists("Black Pepper", "Spices / Herbs");
        createIngredientIfNotExists("Salt", "Spices / Herbs");
        createIngredientIfNotExists("Paprika", "Spices / Herbs");
        createIngredientIfNotExists("Pasta", "Grains / Pasta / Rice");
        createIngredientIfNotExists("Rice", "Grains / Pasta / Rice");
        createIngredientIfNotExists("Bread", "Bread / Bakery");
        createIngredientIfNotExists("Tomato Sauce", "Canned / Jarred");
        createIngredientIfNotExists("Chickpeas", "Canned / Jarred");
        createIngredientIfNotExists("Olive Oil", "Oils / Vinegars");
        createIngredientIfNotExists("Balsamic Vinegar", "Oils / Vinegars");
    }

    private void seedRecipes() {
        createRecipeIfNotExists(
                "Pasta with tomato sauce",
                "1. Boil pasta according to package instructions.\n" +
                "2. In a separate pan, saute garlic and onion in olive oiluntil translucent.\n" +
                "3. Add tomato sauce, oregano, salt and black pepper and simmer for 10 minutes.\n" +
                "3. Toss the cooked pasta with the tomato sauce.\n" +
                "4. Serve hot with parmesan on top and enjoy!",

                5,
                25,
                Difficulty.MEDIUM,
                true,
                false,
                false,
                "admin@recipeapp.com",
                List.of(
                    line("Pasta", "200", Unit.G),
                    line("Tomato Sauce", "150", Unit.ML),
                    line("Garlic", "2", Unit.PCS),
                    line("Onion", "1", Unit.PCS),
                    line("Olive Oil", "2", Unit.TBSP),
                    line("Oregano", "1", Unit.TSP),
                    line("Salt", "1/2", Unit.TSP),
                    line("Black Pepper", "1/2", Unit.TSP),
                    line("Parmesan", "30", Unit.G)
                )
        );

        createRecipeIfNotExists(
            "Chickpea Tomato Rice Bowl", 
            "1. Boil the rice according to package instructions.\n" +
            "2. In a separate pan, saute garlic and onion in olive oil until translucent.\n" +
            "3. Add tomatoes and chickpeas, and simmer for 10 minutes.\n" +
            "4. Serve the rice topped with the tomato and chickpea mixture.",
            5,
            15,
            Difficulty.EASY,
            true,
            true,
            false,
            "admin@recipeapp.com",
            List.of(
                line("Rice", "200", Unit.G),
                line("Tomato", "2", Unit.PCS),
                line("Chickpeas", "150", Unit.G),
                line("Garlic", "2", Unit.PCS),
                line("Onion", "1", Unit.PCS),
                line("Olive Oil", "2", Unit.TBSP),
                line("Oregano", "1", Unit.TSP),
                line("Salt", "1/2", Unit.TSP),
                line("Black Pepper", "1/2", Unit.TSP)
            )
        );

        createRecipeIfNotExists(
            "Chicken and Spinach Stir Fry", 
            "1. Cut the chicken breast into bite-sized pieces.\n" +
            "2. In a pan, heat olive oil and cook the chicken until browned.\n" +
            "3. Add garlic and onion, and cook until fragrant.\n" +
            "4. Add spinach and cook until wilted.\n" +
            "5. Season with salt, black pepper, and paprika.\n" +
            "6. Serve hot with rice or bread.",
            10,
            20,
            Difficulty.MEDIUM,
            false,
            true,
            false,
            "admin@recipeapp.com",
            List.of(
                line("Chicken Breast", "200", Unit.G),
                line("Spinach", "100", Unit.G),
                line("Garlic", "2", Unit.PCS),
                line("Onion", "1", Unit.PCS),
                line("Olive Oil", "2", Unit.TBSP),
                line("Salt", "1/2", Unit.TSP),
                line("Black Pepper", "1/2", Unit.TSP),
                line("Paprika", "1/2", Unit.TSP)
            )
        );

        createRecipeIfNotExists(
            "Tuna Salad", 
            "1. In a bowl, combine drained tuna, chopped onion, and chopped tomato.\n" +
            "2. Add olive oil, balsamic vinegar, salt, and black pepper to taste.\n" +
            "3. Mix well and serve chilled.",
            10,
            0,
            Difficulty.EASY,
            true,
            true,
            true,
            "admin@recipeapp.com",
            List.of(
                line("Tuna", "200", Unit.G),
                line("Onion", "1", Unit.PCS),
                line("Tomato", "2", Unit.PCS),
                line("Olive Oil", "2", Unit.TBSP),
                line("Balsamic Vinegar", "1", Unit.TBSP),
                line("Salt", "1/2", Unit.TSP),
                line("Black Pepper", "1/2", Unit.TSP)
            )
        );
    }   
}



