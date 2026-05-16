package com.elena.recipeapp.recipe.dto;

import java.util.List;

import com.elena.recipeapp.enums.MatchStatus;

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
public class RecipeMatchResponseDTO {

    private RecipeResponseDTO recipe;
    private MatchStatus matchStatus;
    private List<Long> missingIngredientIds;

}
