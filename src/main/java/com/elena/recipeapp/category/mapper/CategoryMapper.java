package com.elena.recipeapp.category.mapper;

import org.mapstruct.Mapper;

import com.elena.recipeapp.category.dto.CategoryRequestDTO;
import com.elena.recipeapp.category.dto.CategoryResponseDTO;
import com.elena.recipeapp.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    Category toEntity(CategoryRequestDTO dto);
    CategoryResponseDTO toDto(Category category);
}
