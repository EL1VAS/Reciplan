package com.elena.recipeapp.shoppinglist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.shoppinglist.model.ShoppingListItem;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    List<ShoppingListItem> findByUserId(Long userId);
    List<ShoppingListItem> findByUserIdOrderByIngredientCategoryNameAsc(Long userId);
    void deleteByUserId(Long userId);
    boolean existsByUserIdAndIngredientId(Long userId, Long ingredientId);
}
