package com.elena.recipeapp.common.exeptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
    
}
