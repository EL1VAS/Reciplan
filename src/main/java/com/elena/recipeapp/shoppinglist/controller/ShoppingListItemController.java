package com.elena.recipeapp.shoppinglist.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elena.recipeapp.common.exceptions.UnauthorizedException;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemPatchDTO;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemRequestDTO;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemResponseDTO;
import com.elena.recipeapp.shoppinglist.service.ShoppingListItemService;
import com.elena.recipeapp.user.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users/{userId}/shopping-list")
@RequiredArgsConstructor
@Tag(name = "Shopping List", description = "Shopping list management endpoints")
public class ShoppingListItemController {

    private final ShoppingListItemService shoppingListItemService;

    @PostMapping
    @Operation(summary = "Add item in the shopping list for user")
    public ResponseEntity<ShoppingListItemResponseDTO> add(
                    @PathVariable Long userId, 
                    @Valid @RequestBody ShoppingListItemRequestDTO dto,
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(shoppingListItemService.add(userId, dto));
    }

    @GetMapping
    @Operation(summary = "Get all items in shopping list for user")
    public ResponseEntity<Page<ShoppingListItemResponseDTO>> getAllForUser(
                    @PathVariable Long userId, 
                    @PageableDefault(size = 20, sort = "ingredient.name") Pageable pageable, 
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.ok(shoppingListItemService.getAllForUser(userId, pageable));
    }

    @GetMapping("/sorted-by-category")
    @Operation(summary = "Get all items in shopping list sorted by category")
    public ResponseEntity<Page<ShoppingListItemResponseDTO>> getAllForUserSortedByCategory(
                    @PathVariable Long userId, 
                    @PageableDefault(size = 20, sort = "ingredient.category.name") Pageable pageable, 
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.ok(shoppingListItemService.getAllForUserSortedByCategory(userId, pageable));
    }

    @GetMapping("/unchecked")
    @Operation(summary = "Get only unchecked items of shopping list")
    public ResponseEntity<List<ShoppingListItemResponseDTO>> getUncheckedForUser(
                    @PathVariable Long userId, 
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.ok(shoppingListItemService.getUncheckedForUser(userId));
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "Update partially an item by id in shopping list")
    public ResponseEntity<ShoppingListItemResponseDTO> patch(
                    @PathVariable Long userId, 
                    @PathVariable Long itemId, 
                    @Valid @RequestBody ShoppingListItemPatchDTO dto, 
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId); 
        return ResponseEntity.ok(shoppingListItemService.patch(userId, itemId, dto));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete item of shopping list by id")
    public ResponseEntity<Void> delete(
                    @PathVariable Long userId, 
                    @PathVariable Long itemId, 
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        shoppingListItemService.delete(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Clear entire shopping list")
    public ResponseEntity<Void> clearAll(
                    @PathVariable Long userId, 
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        shoppingListItemService.clearAll(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/checked")
    @Operation(summary = "Clear all checked items from shopping list")
    public ResponseEntity<Void> clearChecked(
                    @PathVariable Long userId, 
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        shoppingListItemService.clearChecked(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{itemId}/toggle")
    @Operation(summary = "Changes the checked status of an item in the shopping list")
    public ResponseEntity<ShoppingListItemResponseDTO> toggleChecked(
                    @PathVariable Long userId, 
                    @PathVariable Long itemId,
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.ok(shoppingListItemService.toggleChecked(userId, itemId));
    }

    @PostMapping("/from-recipe/{recipeId}")   
    @Operation(summary = "Add missing recipe ingredients to shopping list")
    public ResponseEntity<Void> addFromRecipe(
            @PathVariable Long userId,
            @PathVariable Long recipeId,
            @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        shoppingListItemService.addMissingIngredientsFromRecipe(userId, recipeId);
        return ResponseEntity.noContent().build();
    }

    private void checkOwnership(User requestingUser, Long userId) {
        if (!requestingUser.getId().equals(userId)) {
            throw new UnauthorizedException("You can only access your own shopping list");
        }
    }
}
