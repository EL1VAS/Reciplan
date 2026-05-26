package com.elena.recipeapp.recipe.dto;

import java.math.BigDecimal;

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
public class RecipeIngredientResponseDTO {
    
    private Long ingredientId;
    private String ingredientName;
    private String categoryName;
    private BigDecimal quantity;
    private Unit unit;
}
