package com.elena.recipeapp.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.elena.recipeapp.user.dto.UserPatchDTO;
import com.elena.recipeapp.user.dto.UserResponseDTO;
import com.elena.recipeapp.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserResponseDTO toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void patchUser(UserPatchDTO dto, @MappingTarget User user);

}
