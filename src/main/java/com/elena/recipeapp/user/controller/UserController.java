package com.elena.recipeapp.user.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elena.recipeapp.user.dto.ChangePasswordRequestDTO;
import com.elena.recipeapp.user.dto.UserPatchDTO;
import com.elena.recipeapp.user.dto.UserResponseDTO;
import com.elena.recipeapp.user.model.User;
import com.elena.recipeapp.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management end points")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admin can see the list of users
    @Operation(summary = "Get list of all users (admin only)")
    public ResponseEntity<Page<UserResponseDTO>> getAll(@PageableDefault(size = 20, sort = "username") Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update user by id")
    public ResponseEntity<UserResponseDTO> patch(
                        @PathVariable Long id, 
                        @Valid @RequestBody UserPatchDTO dto, 
                        @AuthenticationPrincipal User requestingUser) {

        return ResponseEntity.ok(userService.patch(id, dto, requestingUser));
    }

    @PutMapping("/{id}/password")
    @ApiResponse(responseCode = "204", description = "Password changed successfully")
    @Operation(summary = "Change user password")
    public ResponseEntity<Void> changePassword(
                        @PathVariable Long id, 
                        @Valid @RequestBody ChangePasswordRequestDTO dto, 
                        @AuthenticationPrincipal User requestingUser) {

        userService.changePassword(id, dto, requestingUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<Void> delete(
                        @PathVariable Long targetId,
                        @AuthenticationPrincipal User requestingUser) {

        userService.delete(requestingUser.getId(), targetId);
        return ResponseEntity.noContent().build();
    }

}
