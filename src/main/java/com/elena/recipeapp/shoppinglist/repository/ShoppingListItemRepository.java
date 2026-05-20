package com.elena.recipeapp.shoppinglist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.shoppinglist.model.ShoppingListItem;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    //The whole shopping list
    @EntityGraph(attributePaths = {"ingredient", "ingredient.category", "user"})
    Page<ShoppingListItem> findByUserId(Long userId, Pageable pageable);

    // Ownership check for patch/delete
    Optional<ShoppingListItem> findByIdAndUserId(Long id, Long userId);

    // For getUnchecked and clearChecked
    List<ShoppingListItem> findByUserIdAndChecked(Long userId, boolean checked);

    //Shopping list by category of ingredients for easier shopping
    List<ShoppingListItem> findByUserIdOrderByIngredientCategoryNameAsc(Long userId);

    //Delete the full list
    void deleteByUserId(Long userId);

    //When we want to search if a specific item is in the list
    boolean existsByUserIdAndIngredientId(Long userId, Long ingredientId);
}
