package com.elena.recipeapp.recipe.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elena.recipeapp.enums.Difficulty;
import com.elena.recipeapp.recipe.dto.RecipeRequestDTO;
import com.elena.recipeapp.recipe.dto.RecipeResponseDTO;
import com.elena.recipeapp.recipe.service.RecipeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipes", description = "Recipe menagement endpoints")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/users/{userId}")
    @Operation(summary = "Create a new recipe")
    public ResponseEntity<RecipeResponseDTO> create(@PathVariable Long userId, @Valid @RequestBody RecipeRequestDTO dto) {
        return ResponseEntity.status(201).body(recipeService.create(userId, dto));
    }

    @GetMapping
    @Operation(summary = "Get all recipes with optional filters")
    public ResponseEntity<Page<RecipeResponseDTO>> getAll(
        @RequestParam(required = false) String title, 
        @RequestParam(required = false) Difficulty difficulty, 
        @RequestParam(required = false) Integer maxPrepTime, 
        @RequestParam(required = false) Boolean vegetarian, 
        @RequestParam(required = false) Boolean lactoseFree, 
        @RequestParam(required = false) Boolean glutenFree, 
        @PageableDefault(size = 10, sort = "title") Pageable pageable) {
            return ResponseEntity.ok(recipeService.getAll(title, difficulty, maxPrepTime, vegetarian, lactoseFree, glutenFree, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by id")
    public ResponseEntity<RecipeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    @GetMapping("/by-ingredient/{ingredientId}")
    @Operation(summary = "Get all recipes that include this ingredient id")
    public ResponseEntity<Page<RecipeResponseDTO>> getByIngredient(@PathVariable Long ingredientId, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(recipeService.getByIngredient(ingredientId, pageable));
    }

    @PatchMapping("/{id}/users/{userId}")
    @Operation(summary = "Update a recipe(for authorized users)")
    public ResponseEntity<RecipeResponseDTO> patch(@PathVariable Long userId, @PathVariable Long id, @Valid @RequestBody RecipeRequestDTO dto) {
        return ResponseEntity.ok(recipeService.patch(userId, id, null));
    }

    @DeleteMapping("/{id}/users/{userId}")
    @Operation(summary = "Delete recipe")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long id) {
        recipeService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }  
}
