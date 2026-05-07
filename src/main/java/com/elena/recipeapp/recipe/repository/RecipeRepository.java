package com.elena.recipeapp.recipe.repository;


import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.enums.Difficulty;
import com.elena.recipeapp.recipe.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findAll(Pageable pageable);
    Page<Recipe> findByVegetarianTrue(Pageable pageable);
    Page<Recipe> findByLactoseFreeTrue(Pageable pageable);
    Page<Recipe> findByGlutenFreeTrue(Pageable pageable);
    Page<Recipe> findByDifficulty(Difficulty difficulty, Pageable pageable);
    Page<Recipe> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Recipe> findByPrepTimeMinutesLessThanEqual(Integer minutes, Pageable pageable);
    Page<Recipe> findByRecipeIngredientsIngredientId(Long ingredientId, Pageable pageable);
    boolean existsByTitleIgnoreCase(String title);

}
