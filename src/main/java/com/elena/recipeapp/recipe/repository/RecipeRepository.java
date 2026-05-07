package com.elena.recipeapp.recipe.repository;


import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.enums.Difficulty;
import com.elena.recipeapp.recipe.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    //All recipes
    Page<Recipe> findAll(Pageable pageable);

    //Filter by dietary conditions
    Page<Recipe> findByVegetarianTrue(Pageable pageable);
    Page<Recipe> findByLactoseFreeTrue(Pageable pageable);
    Page<Recipe> findByGlutenFreeTrue(Pageable pageable);

    //Filter by difficulty
    Page<Recipe> findByDifficulty(Difficulty difficulty, Pageable pageable);

    //Filter by name included in the title
    Page<Recipe> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    //When you want to cook something in less than x minutes
    Page<Recipe> findByPrepTimeMinutesLessThanEqual(Integer minutes, Pageable pageable);

    //When you want to search for all recipes that include a specific ingredient
    Page<Recipe> findByRecipeIngredientsIngredientId(Long ingredientId, Pageable pageable);

    //If this recipe exists
    boolean existsByTitleIgnoreCase(String title);

}
