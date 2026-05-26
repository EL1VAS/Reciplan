package com.elena.recipeapp.shoppinglist.dto;

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
public class ShoppingListItemPatchDTO {

    private Long ingredientId;

    @Positive (message = "Quantity value must be positive")
    private Double quantity;

    private Unit unit;

    private Boolean checked; //not boolean because primitives can't be null
    
}
