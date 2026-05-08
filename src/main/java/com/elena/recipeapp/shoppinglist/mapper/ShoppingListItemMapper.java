package com.elena.recipeapp.shoppinglist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemRequestDTO;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemResponseDTO;
import com.elena.recipeapp.shoppinglist.model.ShoppingListItem;

@Mapper(componentModel = "spring")
public interface ShoppingListItemMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    @Mapping(target = "checked", ignore = true)
    ShoppingListItem toEntity(ShoppingListItemRequestDTO dto);

    @Mapping(source = "ingredient.name", target = "ingredientName")
    @Mapping(source = "ingredient.category.name", target = "categoryName")
    ShoppingListItemResponseDTO toDto(ShoppingListItem item);
}
