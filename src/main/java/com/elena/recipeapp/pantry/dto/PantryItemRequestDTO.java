package com.elena.recipeapp.pantry.dto;

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
public class PantryItemRequestDTO {
    @NotNull
    private Long ingredientId;

    @NotNull
    @Positive
    private Double quantity;

    @NotBlank
    private String unit;
}
