package com.elena.recipeapp.pantry.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.elena.recipeapp.common.exceptions.UnauthorizedException;
import com.elena.recipeapp.pantry.dto.PantryItemPatchDTO;
import com.elena.recipeapp.pantry.dto.PantryItemRequestDTO;
import com.elena.recipeapp.pantry.dto.PantryItemResponseDTO;
import com.elena.recipeapp.pantry.service.PantryItemService;
import com.elena.recipeapp.user.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    public ResponseEntity<PantryItemResponseDTO> add(
                    @PathVariable Long userId, 
                    @Valid @RequestBody PantryItemRequestDTO dto,
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(pantryItemService.add(userId, dto));
    }

    @GetMapping
    @Operation(summary = "Get all ingredients in the pantry for user")
    public ResponseEntity<Page<PantryItemResponseDTO>> getAllForUser(
                    @PathVariable Long userId,
                    @PageableDefault(size = 20, sort = "ingredient.name") Pageable pageable,
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.ok(pantryItemService.getAllForUser(userId, pageable));
    }

    @GetMapping("/sorted-by-category")
    @Operation(summary = "Get all ingredients in the pantry for user sorted by category")
    public ResponseEntity<Page<PantryItemResponseDTO>> getAllForUserSortedByCategory(
                    @PathVariable Long userId, 
                    @PageableDefault(size = 20) Pageable pageable, 
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.ok(pantryItemService.getAllForUserSortedByCategory(userId, pageable));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get all ingredients in pantry for user filtered by category")
    public ResponseEntity<List<PantryItemResponseDTO>> getAllForUserByCategory(
                    @PathVariable Long userId, 
                    @PathVariable Long categoryId,
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.ok(pantryItemService.getAllForUserByCategory(userId, categoryId));
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "Update pantry item")
    public ResponseEntity<PantryItemResponseDTO> patch(
                    @PathVariable Long userId, 
                    @PathVariable Long itemId, 
                    @Valid @RequestBody PantryItemPatchDTO dto,
                    @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        return ResponseEntity.ok(pantryItemService.patch(userId, itemId, dto));
    }

    @DeleteMapping("/{itemId}")
    @ApiResponse(responseCode = "204", description = "Pantry item deleted successfully")
    @Operation(summary = "Delete pantry item")
    public ResponseEntity<Void> delete(
            @PathVariable Long userId, 
            @PathVariable Long itemId,
            @AuthenticationPrincipal User requestingUser) {
        checkOwnership(requestingUser, userId);
        pantryItemService.delete(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    //Helper method to check if the authenticated user is the owner of the pantry they are trying to access
    private void checkOwnership(User requestingUser, Long userId) {
        if (!requestingUser.getId().equals(userId)) {
            throw new UnauthorizedException("You can only access your own pantry");
        }
    }
}