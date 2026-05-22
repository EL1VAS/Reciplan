package com.elena.recipeapp.recipe.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.elena.recipeapp.recipe.dto.RecipeListResponseDTO;
import com.elena.recipeapp.recipe.dto.RecipeMatchResponseDTO;
import com.elena.recipeapp.recipe.dto.RecipePatchDTO;
import com.elena.recipeapp.recipe.dto.RecipeRequestDTO;
import com.elena.recipeapp.recipe.dto.RecipeResponseDTO;
import com.elena.recipeapp.recipe.service.RecipeService;
import com.elena.recipeapp.user.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipes", description = "Recipe menagement endpoints")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/users/{userId}")
    @PreAuthorize("hasRole ('ADMIN')")
    @Operation(summary = "Create a new recipe (Admin only)")
    public ResponseEntity<RecipeResponseDTO> create(
        @Valid @RequestBody RecipeRequestDTO dto,
        @AuthenticationPrincipal User requestingUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.create(requestingUser.getId(), dto));
    }

    @GetMapping
    @Operation(summary = "Get all recipes with optional filters")
    public ResponseEntity<Page<RecipeListResponseDTO>> getAll(
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
    public ResponseEntity<Page<RecipeListResponseDTO>> getByIngredient(
        @PathVariable Long ingredientId, 
        @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(recipeService.getByIngredient(ingredientId, pageable));
    }

    @GetMapping("/matches")
    @Operation(summary = "Get recipes matched against the current user's pantry")
    public ResponseEntity<Page<RecipeMatchResponseDTO>> getMatches(
            @AuthenticationPrincipal User requestingUser,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(recipeService.getMatchesForUser(requestingUser.getId(), pageable));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a recipe(for authorized users (Admin only))")
    public ResponseEntity<RecipeListResponseDTO> patch(
        @PathVariable Long id, 
        @Valid @RequestBody RecipePatchDTO dto,
        @AuthenticationPrincipal User requestingUser) {
        return ResponseEntity.ok(recipeService.patch(requestingUser.getId(), id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete recipe by id (Admin only)")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal User requestingUser) {
        recipeService.delete(requestingUser.getId(), id);
        return ResponseEntity.noContent().build();
    }  
}
