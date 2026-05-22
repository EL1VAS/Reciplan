package com.elena.recipeapp.recipe.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class RecipeIngredientRequestDTO {
    
    @NotNull (message = "Ingredient id can't be null")
    private Long ingredientId;

    @NotNull (message = "Quantity can't be null")
    @Positive (message = "Quantity must have a positive value")
    private BigDecimal quantity;

    @NotBlank (message = "Unit can't be blank")
    private String unit;
}
