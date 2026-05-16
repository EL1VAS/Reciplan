package com.elena.recipeapp.user.dto;

import jakarta.validation.constraints.Email;
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
public class UserPatchDTO {

    //Not added @NotBlank in case they want a partial update of either username or email
    @Size(min = 3, max = 50, message = "Username must be min 3 and max 50 characters")
    private String username;

    @Email (message = "Please enter a valid email")
    private String email;
}
