package com.elena.recipeapp.pantry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.pantry.model.PantryItem;


@Repository
public interface PantryItemRepository extends JpaRepository<PantryItem, Long> {
    
    //All ingredients that user has already
    List<PantryItem> findByUserId(Long userId);

    //Search for a specific ingredient in your pantry
    Optional<PantryItem> findByUserIdAndIngredientId(Long userId, Long ingredientId);

    //All the user's ingredients ordered by category's name
    List<PantryItem> findByUserIdOrderByIngredientCategoryNameAsc(Long userId);

    //All the ingredients of one category in the user's pantry i.e. all dairy products
    List<PantryItem> findByUserIdAndIngredientCategoryId(Long userId, Long categoryId);

    //If a specific ingredient is in the pantry
    boolean existsByUserIdAndIngredientsId(Long userId, Long ingredientsId);

}
