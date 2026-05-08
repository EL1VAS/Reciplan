package com.elena.recipeapp.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.elena.recipeapp.category.dto.CategoryRequestDTO;
import com.elena.recipeapp.category.dto.CategoryResponseDTO;
import com.elena.recipeapp.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequestDTO dto);
    CategoryResponseDTO toDto(Category category);
}
