package com.elena.recipeapp.pantry.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elena.recipeapp.pantry.dto.PantryItemPatchDTO;
import com.elena.recipeapp.pantry.dto.PantryItemRequestDTO;
import com.elena.recipeapp.pantry.dto.PantryItemResponseDTO;
import com.elena.recipeapp.pantry.service.PantryItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users/{userId}/pantry")
@RequiredArgsConstructor
@Tag(name = "Pantry", description = "Pantry management endpoints")
public class PantryItemController {

    private final PantryItemService pantryItemService;

    @PostMapping
    @Operation(summary = "Add ingredient to user's pantry")
    public ResponseEntity<PantryItemResponseDTO> add(@PathVariable Long userId, PantryItemRequestDTO dto) {
        return ResponseEntity.status(201).body(pantryItemService.add(userId, dto));
    }

    @GetMapping
    @Operation(summary = "Get all ingredients in the pantry for user")
    public ResponseEntity<List<PantryItemResponseDTO>> getAllForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(pantryItemService.getAllForUser(userId));
    }

    @GetMapping("/sorted-by-category")
    @Operation(summary = "Get all ingredients in the pantry for user sorted by category")
    public ResponseEntity<List<PantryItemResponseDTO>> getAllForUserSortedByCategory(@PathVariable Long userId) {
        return ResponseEntity.ok(pantryItemService.getAllForUserSortedByCategory(userId));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get all ingredients in pantry for user filtered by category")
    public ResponseEntity<List<PantryItemResponseDTO>> getAllForUserByCategory(@PathVariable Long userId, @PathVariable Long categoryId) {
        return ResponseEntity.ok(pantryItemService.getAllForUserByCategory(userId, categoryId));
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "Update pantry item")
    public ResponseEntity<PantryItemResponseDTO> patch(@PathVariable Long userId, @PathVariable Long itemId, @Valid @RequestBody PantryItemPatchDTO dto) {
        return ResponseEntity.ok(pantryItemService.patch(userId, itemId, dto));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete pantry item")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long itemId) {
        pantryItemService.delete(userId, itemId);
        return ResponseEntity.noContent().build();
    }
}
