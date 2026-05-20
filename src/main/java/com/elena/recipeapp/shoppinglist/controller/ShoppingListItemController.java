package com.elena.recipeapp.shoppinglist.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemPatchDTO;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemRequestDTO;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemResponseDTO;
import com.elena.recipeapp.shoppinglist.service.ShoppingListItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users/{userId}/shopping-list")
@RequiredArgsConstructor
@Tag(name = "Shopping List", description = "Shopping list management endpoints")
public class ShoppingListItemController {

    private final ShoppingListItemService shoppingListItemService;

    @PostMapping
    @Operation(summary = "Add item in the shopping list for user")
    public ResponseEntity<ShoppingListItemResponseDTO> add(@PathVariable Long userId, @Valid @RequestBody ShoppingListItemRequestDTO dto) {
        return ResponseEntity.status(201).body(shoppingListItemService.add(userId, dto));
    }

    @GetMapping
    @Operation(summary = "Get all items in shopping list for user")
    public ResponseEntity<List<ShoppingListItemResponseDTO>> getAllForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingListItemService.getAllForUser(userId));
    }

    @GetMapping("/sorted-by-category")
    @Operation(summary = "Get all items in shopping list sorted by category")
    public ResponseEntity<List<ShoppingListItemResponseDTO>> getAllForUserSortedByCategory(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingListItemService.getAllForUserSortedByCategory(userId));
    }

    @GetMapping("/unchecked")
    @Operation(summary = "Get only unchecked items of shopping list")
    public ResponseEntity<List<ShoppingListItemResponseDTO>> getUncheckedForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingListItemService.getUncheckedForUser(userId));
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "Update partially an item by id in shopping list")
    public ResponseEntity<ShoppingListItemResponseDTO> patch(@PathVariable Long userId, @PathVariable Long itemId, @Valid @RequestBody ShoppingListItemPatchDTO dto) {
        return ResponseEntity.ok(shoppingListItemService.patch(userId, itemId, dto));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete item of shopping list by id")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long itemId) {
        shoppingListItemService.delete(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Clear entire shopping list")
    public ResponseEntity<Void> clearAll(@PathVariable Long userId) {
        shoppingListItemService.clearAll(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/checked")
    @Operation(summary = "Clear all checked items from shopping list")
    public ResponseEntity<Void> clearChecked(@PathVariable Long userId) {
        shoppingListItemService.clearChecked(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{itemId}/toggle")
    @Operation(summary = "Changes the checked status of an item in the shopping list")
    public ResponseEntity<ShoppingListItemResponseDTO> toggleChecked(@PathVariable Long userId, @PathVariable Long itemId) {
        return ResponseEntity.ok(shoppingListItemService.toggleChecked(userId, itemId));
    }

    
}
