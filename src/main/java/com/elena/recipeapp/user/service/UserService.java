package com.elena.recipeapp.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elena.recipeapp.common.exceptions.InvalidPasswordException;
import com.elena.recipeapp.common.exceptions.ResourceNotFoundException;
import com.elena.recipeapp.common.exceptions.UnauthorizedException;
import com.elena.recipeapp.enums.Roles;
import com.elena.recipeapp.user.dto.ChangePasswordRequestDTO;
import com.elena.recipeapp.user.dto.UserPatchDTO;
import com.elena.recipeapp.user.dto.UserResponseDTO;
import com.elena.recipeapp.user.mapper.UserMapper;
import com.elena.recipeapp.user.model.User;
import com.elena.recipeapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponseDTO> getAll(Pageable pageable) {
        log.info("Fetching all users - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable).map(userMapper :: toDto);
    }

    public UserResponseDTO getById(Long id) {
        log.info("Fetching user with id: {}", id);

        User user = findUserById(id);
        return userMapper.toDto(user);
    }

    @Transactional
    public UserResponseDTO patch(Long id, UserPatchDTO dto, User requestingUser) {
        log.info("Updating user with id: {}", id);

        // only the user themselves or an admin can patch
        if (!requestingUser.getId().equals(id) && !requestingUser.getRole().equals(Roles.ADMIN)) {
            throw new UnauthorizedException("You can only update your own account");
        }

        User user = findUserById(id);
        userMapper.patchUser(dto, user);
        User saved = userRepository.save(user);
        log.debug("Updated user with id: {}", id);
        return userMapper.toDto(saved);
    }

    @Transactional
    public void changePassword(Long id, ChangePasswordRequestDTO dto, User requestingUser) {
        log.info("Changing password for user with id: {}", id);

        // only the account owner can change their own password
        if (!requestingUser.getId().equals(id)) {
            throw new UnauthorizedException("You can only change your own password");
        }

        User user = findUserById(id);

        if(!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            log.warn("Password change failed for user with id: {} - wrong current password", id);
            throw new InvalidPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        log.debug("Password changed for user with id: {}", id);
    }

    @Transactional
    public void delete(Long requestingUserId, Long targetId) {
        
        User requesting = findUserById(requestingUserId);

        // allow if deleting own account OR if admin
        if (!requestingUserId.equals(targetId) && !requesting.getRole().equals(Roles.ADMIN)) {
            throw new UnauthorizedException("You can only delete your own account");
        }

        User user = findUserById(targetId);
        userRepository.delete(user);
}

    //Helper method
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
    }
    
}
