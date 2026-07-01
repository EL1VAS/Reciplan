package com.elena.recipeapp.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameIgnoreCase(String name);
    
}
