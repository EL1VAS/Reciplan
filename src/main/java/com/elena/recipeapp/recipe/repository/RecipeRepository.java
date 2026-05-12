package com.elena.recipeapp.recipe.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.recipe.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    //When you want to search for all recipes that include a specific ingredient
    Page<Recipe> findByRecipeIngredientsIngredientId(Long ingredientId, Pageable pageable);

    //If this recipe exists
    boolean existsByTitleIgnoreCase(String title);

    // Check if recipe belongs to user (for ownership check)
    boolean existsByIdAndCreatedById(Long id, Long userId);

}
