package com.elena.recipeapp.recipe.repository;


import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.recipe.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    // Loads createdBy, but NOT recipeIngredients — handled separately
    @EntityGraph(attributePaths = {"createdBy"})
    Page<Recipe> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy"})
    Optional<Recipe> findById(Long id);

    //When you want to search for all recipes that include a specific ingredient
    Page<Recipe> findByRecipeIngredientsIngredientId(Long ingredientId, Pageable pageable);

    //If this recipe exists
    boolean existsByTitleIgnoreCase(String title);

    // Check if recipe belongs to user (for ownership check)
    boolean existsByIdAndCreatedById(Long id, Long userId);

}
