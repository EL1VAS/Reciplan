package com.elena.recipeapp.recipe.repository;


import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.recipe.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    // Used by: getAll (no filter) — kept for completeness
    @EntityGraph(attributePaths = {"createdBy"})
    Page<Recipe> findAll(Pageable pageable);

    // Loads createdBy, but NOT recipeIngredients — handled separately used for filtering
    // Used by: getAll (filtered)
    @EntityGraph("Recipe.withCreatedBy")
    Page<Recipe> findAll(Specification<Recipe> spec, Pageable pageable);

    // Used by: getMatchesForUser 
    @Query("SELECT r FROM Recipe r") //used to stop Spring Data to try parse from the name
    @EntityGraph(attributePaths = {"createdBy", "recipeIngredients", "recipeIngredients.ingredient"})
    List<Recipe> findAllWithIngredients();

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
