package com.elena.recipeapp.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elena.recipeapp.category.dto.CategoryRequestDTO;
import com.elena.recipeapp.category.dto.CategoryResponseDTO;
import com.elena.recipeapp.category.mapper.CategoryMapper;
import com.elena.recipeapp.category.model.Category;
import com.elena.recipeapp.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        log.info("Creating category with name: {}", dto.getName()); // info for important operations

        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);

        log.debug("Created category with id: {}", saved.getId()); //debug for more detailed info
        return categoryMapper.toDto(saved);
    }

    public List<CategoryResponseDTO> getAll() {
        log.info("Fetching all categories");
        return categoryRepository.findAll().stream().map(categoryMapper ::toDto).toList();
    }

    public CategoryResponseDTO getById(Long id) {
        log.info("Fetching category with id: {}", id);

        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Category not found with id: {}", id);
            return new RuntimeException("Category not found with id: " + id);
        });
        return categoryMapper.toDto(category);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        log.info("Updating category with id: {}", id);

        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Category not found with id: {}", id);
            return new RuntimeException("Category not found with id: " + id);
        });

        category.setName(dto.getName());
        Category saved = categoryRepository.save(category);

        log.debug("Category updated: {}", saved.getName());
        return categoryMapper.toDto(saved);
    }

    public void delete(Long id) {
        log.info("Deleting category with id: {}", id);

        if(!categoryRepository.existsById(id)) {
            log.warn("Attempted to delete non existing category with id: {}", id);
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
    
}
