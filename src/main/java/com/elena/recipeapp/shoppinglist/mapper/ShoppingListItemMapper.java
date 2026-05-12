package com.elena.recipeapp.shoppinglist.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemPatchDTO;
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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    void patchShoppingListItem(ShoppingListItemPatchDTO dto, @MappingTarget ShoppingListItem item);
}
