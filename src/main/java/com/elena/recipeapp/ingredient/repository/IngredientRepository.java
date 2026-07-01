package com.elena.recipeapp.ingredient.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.ingredient.model.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @EntityGraph(attributePaths = {"category"})
    Page<Ingredient> findAll(Pageable pageable);  // overrides JpaRepository's findAll()

    @EntityGraph(attributePaths = {"category"})
    Optional<Ingredient> findById(Long id);

    @EntityGraph(attributePaths = {"category"})
    Optional<Ingredient> findByName(String name);

    @EntityGraph(attributePaths = {"category"})
    Optional<Ingredient> findByNameIgnoreCase(String name);

    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"category"})
    List<Ingredient> findByCategoryId(Long categoryId);

}
