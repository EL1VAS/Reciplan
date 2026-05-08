package com.elena.recipeapp.user.mapper;

import org.mapstruct.Mapper;

import com.elena.recipeapp.user.dto.UserResponseDTO;
import com.elena.recipeapp.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserResponseDTO toDto(User user);
    
}
