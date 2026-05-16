package com.elena.recipeapp.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import com.elena.recipeapp.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management end points")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get list of all users")
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update user by id")
    public ResponseEntity<UserResponseDTO> patch(@PathVariable Long id, @Valid @RequestBody UserPatchDTO dto) {
        return ResponseEntity.ok(userService.patch(id, dto));
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Change user password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequestDTO dto) {
        userService.changePassword(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<Void> delete(@PathVariable Log id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
