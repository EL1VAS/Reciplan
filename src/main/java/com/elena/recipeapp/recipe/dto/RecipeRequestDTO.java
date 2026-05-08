package com.elena.recipeapp.recipe.dto;

import java.util.List;

import com.elena.recipeapp.enums.Difficulty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class RecipeRequestDTO {

    @NotBlank
    @Size(max = 50)
    private String title;

    private String instructions;

    @Min(1)
    private Integer prepTimeMinutes;

    @Min(1)
    private Integer cookTimeMinutes;

    @NotNull
    private Difficulty difficulty;

    private boolean vegetarian;
    private boolean lactoseFree;
    private boolean glutenFree;

    @NotEmpty
    private List<RecipeIngredientRequestDTO> recipeIngredients;
}
