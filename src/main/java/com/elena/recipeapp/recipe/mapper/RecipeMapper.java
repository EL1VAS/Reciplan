package com.elena.recipeapp.recipe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.elena.recipeapp.recipe.dto.RecipeIngredientResponseDTO;
import com.elena.recipeapp.recipe.dto.RecipeRequestDTO;
import com.elena.recipeapp.recipe.dto.RecipeResponseDTO;
import com.elena.recipeapp.recipe.model.Recipe;
import com.elena.recipeapp.recipe.model.RecipeIngredients;


@Mapper(componentModel = "spring")
public interface RecipeMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "instructions", ignore = true)
    Recipe toEntity(RecipeRequestDTO dto);

    @Mapping(source = "createdBy.username", target = "createdByUsername")
    RecipeResponseDTO toDto(Recipe recipe);

    @Mapping(source = "ingredient.id", target = "ingredientId")
    @Mapping(source = "ingredient.name", target = "ingredientName")
    @Mapping(source = "ingredient.category.name", target = "categoryName")
    RecipeIngredientResponseDTO toDto(RecipeIngredients recipeIngredients);
}
