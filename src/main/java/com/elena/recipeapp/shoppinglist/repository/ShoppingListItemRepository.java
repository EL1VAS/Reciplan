package com.elena.recipeapp.shoppinglist.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.shoppinglist.model.ShoppingListItem;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    //The whole shopping list
    @EntityGraph(attributePaths = {"ingredient", "ingredient.category"})
    Page<ShoppingListItem> findByUserId(Long userId, Pageable pageable);

    // Ownership check for patch/delete
    @EntityGraph(attributePaths = {"ingredient", "ingredient.category"})
    Optional<ShoppingListItem> findByIdAndUserId(Long id, Long userId);

    // For getUnchecked and clearChecked
    @EntityGraph(attributePaths = {"ingredient", "ingredient.category"})
    List<ShoppingListItem> findByUserIdAndChecked(Long userId, boolean checked);

    //Shopping list by category of ingredients for easier shopping
    @EntityGraph(attributePaths = {"ingredient", "ingredient.category"})
    Page<ShoppingListItem> findByUserIdOrderByIngredientCategoryNameAsc(Long userId, Pageable pageable);

    //Delete the full list
    void deleteByUserId(Long userId);

    //When we want to search if a specific item is in the list 
    boolean existsByUserIdAndIngredientId(Long userId, Long ingredientId);

}
