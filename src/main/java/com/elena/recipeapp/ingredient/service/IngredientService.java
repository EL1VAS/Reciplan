package com.elena.recipeapp.ingredient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elena.recipeapp.category.repository.CategoryRepository;
import com.elena.recipeapp.category.model.Category;
import com.elena.recipeapp.ingredient.dto.IngredientRequestDTO;
import com.elena.recipeapp.ingredient.dto.IngredientResponseDTO;
import com.elena.recipeapp.ingredient.mapper.IngredientMapper;
import com.elena.recipeapp.ingredient.model.Ingredient;
import com.elena.recipeapp.ingredient.repository.IngredientRepository;
import com.elena.recipeapp.common.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    public IngredientResponseDTO create(IngredientRequestDTO dto) {
        log.info("Creating ingredient with name: {}", dto.getName());

        Category category = findCategoryById(dto.getCategoryId());

        Ingredient ingredient = ingredientMapper.toEntity(dto);
        ingredient.setCategory(category);

        Ingredient saved = ingredientRepository.save(ingredient);
        log.debug("Ingredient saved with id: {}", ingredient.getId());

        return ingredientMapper.toDto(saved);
    }

    public List<IngredientResponseDTO> getAll() {
        log.info("Fetching all ingredients");
        
        return ingredientRepository.findAll().stream().map(ingredientMapper :: toDto).toList();
    }
    
    public IngredientResponseDTO getById(Long id) {
        log.info("Fetching ingredient with id: {}", id);

        Ingredient ingredient = findIngredientById(id);
        return ingredientMapper.toDto(ingredient);
    }

    public IngredientResponseDTO update(Long id, IngredientRequestDTO dto) {
        log.info("Updating ingredient with id: {}", id);

        Ingredient ingredient = findIngredientById(id);

        Category category = findCategoryById(dto.getCategoryId());

        ingredient.setName(dto.getName());
        ingredient.setCategory(category);

        Ingredient saved = ingredientRepository.save(ingredient);
        log.debug("Ingredient updated: ", saved.getName());

        return ingredientMapper.toDto(saved);
    }

    public void delete(Long id) {
        log.info("Deleting ingredient with id: {}", id);
        Ingredient ingredient = findIngredientById(id);
        ingredientRepository.delete(ingredient);

        log.debug("Ingredient deleted with id: {}", id);
    }

    //Helper methods
    private Ingredient findIngredientById(Long id) {
        return ingredientRepository.findById(id).orElseThrow(() -> {
            log.warn("Ingredient not found with id: {}", id);
            return new ResourceNotFoundException("Ingredient not found with id: " + id);
        });
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> {
            log.warn("Category not found with id: {}", categoryId);
            return new ResourceNotFoundException("Category not found with id: " + categoryId);
        });
    }


    
}
