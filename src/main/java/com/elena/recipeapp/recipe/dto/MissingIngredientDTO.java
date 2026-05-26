package com.elena.recipeapp.recipe.dto;

import com.elena.recipeapp.enums.Unit;

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
public class MissingIngredientDTO {

    private Long ingredientId;
    private String ingredientName;
    private double missingQuantity;
    private Unit unit;
    
}
