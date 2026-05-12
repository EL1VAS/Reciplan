package com.elena.recipeapp.recipe.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elena.recipeapp.ingredient.model.Ingredient;
import com.elena.recipeapp.ingredient.repository.IngredientRepository;
import com.elena.recipeapp.recipe.dto.RecipeIngredientRequestDTO;
import com.elena.recipeapp.recipe.dto.RecipeRequestDTO;
import com.elena.recipeapp.recipe.dto.RecipeResponseDTO;
import com.elena.recipeapp.recipe.mapper.RecipeMapper;
import com.elena.recipeapp.recipe.model.Recipe;
import com.elena.recipeapp.recipe.model.RecipeIngredients;
import com.elena.recipeapp.recipe.repository.RecipeIngredientsRepository;
import com.elena.recipeapp.recipe.repository.RecipeRepository;
import com.elena.recipeapp.user.model.User;
import com.elena.recipeapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {
    
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public RecipeResponseDTO create(Long userId, RecipeRequestDTO dto) {
        log.info("Creating recipe: {} by user id: {}", dto.getTitle(), userId);

        User user = 


    }

    //Helper methods
    private Recipe findRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> {
            log.warn("Recipe not found with id: {}", id);
            return new RuntimeException("Recipe not found with id: " + id);
        });
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User not found with id: {}", userId);
            return new RuntimeException("User not found with id: " + userId);
        });
    }

    private void checkOwnership(Long userId, Long id) {
        if (!recipeRepository.existsByIdAndCreatedById(id, userId)) {
            log.warn("User with id: {} attempted to modify recipe with id: {} they don't own", userId, id);
            throw new RuntimeException("You are not authorized to modify this recipe");
        }
    }

    private List<RecipeIngredients> buildIngredientLines (List<RecipeIngredientRequestDTO> dtos, Recipe recipe) {
        return dtos.stream().map(dto -> {
            Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId()).orElseThrow(()-> {
                log.warn("Ingredient not found with id: {}", dto.getIngredientId());
                        return new RuntimeException("Ingredient not found with id: " + dto.getIngredientId());
            });
            return RecipeIngredients.builder()
                                    .recipe(recipe)
                                    .ingredient(ingredient)
                                    .quantity(dto.getQuantity())
                                    .unit(dto.getUnit())
                                    .build();
            }).toList();        
        }
        
}
