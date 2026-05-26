package com.elena.recipeapp.pantry.dto;

import com.elena.recipeapp.enums.Unit;

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
public class PantryItemPatchDTO {

    private Long ingredientId;

    @Positive (message = "Quantity must have a positive value")
    private Double quantity;

    private Unit unit;
    
}
