package com.elena.recipeapp.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elena.recipeapp.user.dto.UserResponseDTO;
import com.elena.recipeapp.user.mapper.UserMapper;
import com.elena.recipeapp.user.model.User;
import com.elena.recipeapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getAll() {
        log.info("Fetching all users");
        return userRepository.findAll().stream().map(userMapper :: toDto).toList();
    }

    public UserResponseDTO getById(Long id){
        log.info("Fetching user with id: {}", id);

        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("Usern not found with id: {}", id);
            return new RuntimeException("User not found with id: " + id)
        });
        return userMapper.toDto(user);
    }


    
}
