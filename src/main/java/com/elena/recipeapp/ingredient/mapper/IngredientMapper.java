package com.elena.recipeapp.ingredient.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.elena.recipeapp.ingredient.dto.IngredientRequestDTO;
import com.elena.recipeapp.ingredient.dto.IngredientResponseDTO;
import com.elena.recipeapp.ingredient.model.Ingredient;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    
    @Mapping(target = "category", ignore = true)
    Ingredient toEntity(IngredientRequestDTO dto);

    @Mapping(source = "category.name", target = "categoryName")
    IngredientResponseDTO toDto(Ingredient ingredient);
}
