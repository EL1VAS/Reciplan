package com.elena.recipeapp.ingredient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class IngredientRequestDTO {
    @NotBlank (message = "Ingredient name can't be blank")
    @Size(min = 2, max = 50, message = "Ingredient name must be min 2 and max 50 characters") 
    private String name;

    @NotNull (message = "Category id can't be blank")
    private Long categoryId;
}
