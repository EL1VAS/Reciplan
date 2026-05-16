package com.elena.recipeapp.shoppinglist.dto;

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
public class ShoppingListItemRequestDTO {
    
    @NotNull (message = "Ingredient id can't be null")
    private Long ingredientId;

    @NotNull (message = "Quantity can't be null")
    @Positive (message = "Quantity value must be positive")
    private Double quantity;

    @NotBlank (message = "Unit can't be blank")
    private String unit;
}
