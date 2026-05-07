package com.elena.recipeapp.pantry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.pantry.model.PantryItem;


@Repository
public interface PantryItemRepository extends JpaRepository<PantryItem, Long> {
    List<PantryItem> findByUserId(Long userId);
    Optional<PantryItem> findByUserIdAndIngredientId(Long userId, Long ingredientId);
    List<PantryItem> findByUserIdOrderByIngredientCategoryNameAsc(Long userId);
    List<PantryItem> findByUserIdAndIngredientCategoryId(Long userId, Long categoryId);
    boolean existsByUserIdAndIngredientsId(Long userId, Long ingredientsId);
    void deleteByUserIdAndIngredientId(Long userId, Long ingredientsId);
}
