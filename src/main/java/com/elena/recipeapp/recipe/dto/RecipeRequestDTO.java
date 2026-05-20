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

    @NotBlank (message = "Title can't be blank")
    @Size(max = 50, message = "Title must be max 50 characters")
    private String title;

    private String instructions;

    @Min(value = 1, message = "PrepTimeMinutes value must be min 1")
    private Integer prepTimeMinutes;

    @Min(value = 1, message = "CookTimeMinutes value must be min 1")
    private Integer cookTimeMinutes;

    @NotNull (message = "Difficulty can't be null")
    private Difficulty difficulty;

    private boolean vegetarian;
    private boolean lactoseFree;
    private boolean glutenFree;

    @NotEmpty (message = "RecipeIngredients list can't be empty")
    private List<RecipeIngredientRequestDTO> recipeIngredients;
}
