package com.elena.recipeapp.pantry.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.elena.recipeapp.pantry.dto.PantryItemPatchDTO;
import com.elena.recipeapp.pantry.dto.PantryItemRequestDTO;
import com.elena.recipeapp.pantry.dto.PantryItemResponseDTO;
import com.elena.recipeapp.pantry.model.PantryItem;

@Mapper(componentModel = "spring")
public interface PantryItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    PantryItem toEntity(PantryItemRequestDTO dto);

    @Mapping(source = "ingredient.name", target = "ingredientName")
    @Mapping(source = "ingredient.category.name", target = "categoryName")
    PantryItemResponseDTO toDto(PantryItem pantryItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    void patchPantryItem(PantryItemPatchDTO dto, @MappingTarget PantryItem item);
}
