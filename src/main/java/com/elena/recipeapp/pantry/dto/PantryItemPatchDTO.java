package com.elena.recipeapp.pantry.dto;

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

    @Positive
    private Double quantity;

    private String unit;
    
}
