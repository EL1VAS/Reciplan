package com.elena.recipeapp.pantry.dto;

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
public class PantryItemResponseDTO {

    private Long id;
    private String ingredientName;
    private String categoryName;
    private Double quantity;
    private Unit unit;
}
