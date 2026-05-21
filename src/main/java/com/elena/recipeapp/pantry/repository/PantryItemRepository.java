package com.elena.recipeapp.pantry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.pantry.model.PantryItem;

@Repository
public interface PantryItemRepository extends JpaRepository<PantryItem, Long> {
    
    //All ingredients that user has already
    @EntityGraph(attributePaths = {"ingredient", "ingredient.category"})
    Page<PantryItem> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"ingredient", "ingredient.category"})
    Optional<PantryItem> findByIdAndUserId(Long id, Long userId);

    //Search for a specific ingredient in your pantry
    Optional<PantryItem> findByUserIdAndIngredientId(Long userId, Long ingredientId);

    //All the user's ingredients ordered by category's name
    @EntityGraph(attributePaths = {"ingredient", "ingredient.category"})
    Page<PantryItem> findByUserIdOrderByIngredientCategoryNameAsc(Long userId, Pageable pageable);

    //All the ingredients of one category in the user's pantry i.e. all dairy products
    @EntityGraph(attributePaths = {"ingredient", "ingredient.category"})
    List<PantryItem> findByUserIdAndIngredientCategoryId(Long userId, Long categoryId);

    //If a specific ingredient is in the pantry
    boolean existsByUserIdAndIngredientId(Long userId, Long ingredientId);

}
