package com.elena.recipeapp.pantry.dto;

import com.elena.recipeapp.enums.Unit;

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
public class PantryItemRequestDTO {
    
    @NotNull (message = "Ingredient is can't be null")
    private Long ingredientId;

    @NotNull (message = "Quantity can't be null")
    @Positive (message = "Quantity must have a positive value")
    private Double quantity;

    @NotNull (message = "Unit can't be null")
    private Unit unit;
}
