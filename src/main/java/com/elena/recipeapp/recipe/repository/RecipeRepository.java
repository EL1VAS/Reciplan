package com.elena.recipeapp.recipe.repository;


import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.recipe.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    // Called by: recipeService.getAll(pageable) — no filters
    @EntityGraph(attributePaths = {"createdBy"})
    Page<Recipe> findAll(Pageable pageable);

    // Loads createdBy, but NOT recipeIngredients — handled separately used for filtering
    // No method conflict as they have different parameters (Specification vs Pageable)
    @EntityGraph("Recipe.withCreatedBy")
    Page<Recipe> findAll(Specification<Recipe> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy"})
    Optional<Recipe> findById(Long id);

    //When you want to search for all recipes that include a specific ingredient
    @EntityGraph(attributePaths = {"createdBy"})
    Page<Recipe> findByRecipeIngredientsIngredientId(Long ingredientId, Pageable pageable);

    //If this recipe exists
    boolean existsByTitleIgnoreCase(String title);

    // Check if recipe belongs to user (for ownership check)
    boolean existsByIdAndCreatedById(Long id, Long userId);

}
