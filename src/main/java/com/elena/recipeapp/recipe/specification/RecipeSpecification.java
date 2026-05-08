package com.elena.recipeapp.recipe.specification;

import org.springframework.data.jpa.domain.Specification;

import com.elena.recipeapp.enums.Difficulty;
import com.elena.recipeapp.recipe.model.Recipe;

public class RecipeSpecification {
    
    public static Specification<Recipe> isVegetarian() {
        return (root, query, cb) -> cb.isTrue(root.get("vegetarian"));
    }

    public static Specification<Recipe> isLactoseFree() {
        return (root, query, cb) -> cb.isTrue(root.get("lactoseFree"));
    }

    public static Specification<Recipe> isGlutenFree() {
        return (root, query, cb) -> cb.isTrue(root.get("glutenFree"));
    }

    public static Specification<Recipe> hasTitle(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Recipe> hasDifficulty(Difficulty difficulty) {
        return (root, query, cb) -> cb.equal(root.get("difficulty"), difficulty);
    }

    public static Specification<Recipe> maxPrepTime(Integer minutes) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("prepTimeMinutes"), minutes);
    }

}
