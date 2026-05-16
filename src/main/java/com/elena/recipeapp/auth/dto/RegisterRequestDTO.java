package com.elena.recipeapp.auth.dto;

import jakarta.validation.constraints.Email;
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
public class RegisterRequestDTO {
    
    @NotBlank (message = "Username can't be blank")
    @Size(min = 3, max = 50, message = "Please enter a username with min 3 and max 50 characters")
    private String username;

    @NotBlank (message = "Email can't be blank")
    @Email (message = "Please enter a valid email")
    private String email;

    @NotBlank (message = "Password can't be blank")
    @Size(min = 8, message = "Please enter a password with min 8 characters")
    private String password;
}
