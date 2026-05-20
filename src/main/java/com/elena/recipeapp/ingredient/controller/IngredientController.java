package com.elena.recipeapp.ingredient.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elena.recipeapp.ingredient.dto.IngredientRequestDTO;
import com.elena.recipeapp.ingredient.dto.IngredientResponseDTO;
import com.elena.recipeapp.ingredient.service.IngredientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@Tag(name = "Ingredients", description = "Ingredient management endpoints")
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    @Operation(summary = "Create a new ingredient")
    public ResponseEntity<IngredientResponseDTO> create(@Valid @RequestBody IngredientRequestDTO dto) {
        return ResponseEntity.status(201).body(ingredientService.create(dto));
    }    

    @GetMapping
    @Operation(summary = "Get list of all ingredients")
    public ResponseEntity<List<IngredientResponseDTO>> getAll() {
        return ResponseEntity.ok(ingredientService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ingredient by id")
    public ResponseEntity<IngredientResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getById(id));
    }

    @PutMapping
    @Operation(summary = "Update an ingredient by id")
    public ResponseEntity<IngredientResponseDTO> update(@PathVariable Long id, @Valid @RequestBody IngredientRequestDTO dto) {
        return ResponseEntity.ok(ingredientService.update(id, dto));
    }

    @DeleteMapping
    @Operation(summary = "Delete ingredient by id") 
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
