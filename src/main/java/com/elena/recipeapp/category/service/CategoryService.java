package com.elena.recipeapp.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elena.recipeapp.category.dto.CategoryRequestDTO;
import com.elena.recipeapp.category.dto.CategoryResponseDTO;
import com.elena.recipeapp.category.mapper.CategoryMapper;
import com.elena.recipeapp.category.model.Category;
import com.elena.recipeapp.category.repository.CategoryRepository;
import com.elena.recipeapp.common.exceptions.ResourceNotFoundException;

import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
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

        Category category = findCategoryById(id);
        return categoryMapper.toDto(category);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        log.info("Updating category with id: {}", id);

        Category category = findCategoryById(id);

        category.setName(dto.getName());
        Category saved = categoryRepository.save(category);

        log.debug("Category updated: {}", saved.getName());
        return categoryMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting category with id: {}", id);

        Category category = findCategoryById(id);
        categoryRepository.delete(category);
        log.debug("Deleted category with id: {}", id);
    }

    //Helper method
    private Category findCategoryById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Category not found with id: {}", id);
            return new ResourceNotFoundException("Category not found with id: " + id);
        });
    }
}