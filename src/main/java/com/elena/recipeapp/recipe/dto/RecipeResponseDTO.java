package com.elena.recipeapp.recipe.dto;

import java.util.List;

import com.elena.recipeapp.enums.Difficulty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponseDTO {
    private Long id;
    private String title;
    private String instructions;
    private Integer prepTimeMinutes;
    private Integer cookTimeMinutes;
    private Difficulty difficulty;
    private boolean vegetarian;
    private boolean lactoseFree;
    private boolean glutenFree;
    private String createdByUsername;
    private List<RecipeIngredientResponseDTO> recipeIngredients;
}
