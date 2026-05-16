package com.elena.recipeapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequestDTO {
    
    @NotBlank (message = "Current password can't be blank")
    private String currentPassword;

    @NotBlank (message = "New password can't be blank")
    @Size(min = 8, message = "Please enter a valid new password with min 8 characters")
    private String newPassword;
}
