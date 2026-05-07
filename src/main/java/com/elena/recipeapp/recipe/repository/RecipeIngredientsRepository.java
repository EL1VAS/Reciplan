package com.elena.recipeapp.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.recipe.model.RecipeIngredients;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Long> {

    //Ingredients list per recipe
    List<RecipeIngredients> findByRecipeId(Long recipeId);
}
