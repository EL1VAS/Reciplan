package com.elena.recipeapp.recipe.dto;

import java.util.List;

import com.elena.recipeapp.enums.Difficulty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
public class RecipePatchDTO {

    @Size(max = 50)
    private String title;

    private String instructions;

    @Min(1)
    private Integer prepTimeMinutes;

    @Min(1)
    private Integer cookTimeMinutes;

    private Difficulty difficulty;

    private Boolean vegetarian;
    private Boolean lactoseFree;
    private Boolean glutenFree;

    private List<RecipeIngredientRequestDTO> recipeIngredients;
    
}
