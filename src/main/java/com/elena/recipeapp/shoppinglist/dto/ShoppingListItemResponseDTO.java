package com.elena.recipeapp.shoppinglist.dto;

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
public class ShoppingListItemResponseDTO {
    
    private Long id;
    private String ingredientName;
    private String categoryName;
    private Double quantity;
    private String unit;
    private boolean checked;
}
