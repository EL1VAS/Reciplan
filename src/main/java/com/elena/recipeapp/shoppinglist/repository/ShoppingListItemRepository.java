package com.elena.recipeapp.shoppinglist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.shoppinglist.model.ShoppingListItem;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    //The whole shopping list
    List<ShoppingListItem> findByUserId(Long userId);

    //Shopping list by category of ingredients for easier shopping
    List<ShoppingListItem> findByUserIdOrderByIngredientCategoryNameAsc(Long userId);

    //Delete the full list
    void deleteByUserId(Long userId);

    //When we want to search if a specific item is in the list
    boolean existsByUserIdAndIngredientId(Long userId, Long ingredientId);
}
