package com.elena.recipeapp.common.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponseDTO {
    
    private int status;

    private String message;

    private LocalDateTime timestamp;
    
}
