package com.elena.recipeapp.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elena.recipeapp.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    
    //When we want to just check if user exists without calling the full table row and then discard it
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
