package com.elena.recipeapp.pantry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elena.recipeapp.ingredient.model.Ingredient;
import com.elena.recipeapp.ingredient.repository.IngredientRepository;
import com.elena.recipeapp.pantry.dto.PantryItemPatchDTO;
import com.elena.recipeapp.pantry.dto.PantryItemRequestDTO;
import com.elena.recipeapp.pantry.dto.PantryItemResponseDTO;
import com.elena.recipeapp.pantry.mapper.PantryItemMapper;
import com.elena.recipeapp.pantry.model.PantryItem;
import com.elena.recipeapp.pantry.repository.PantryItemRepository;
import com.elena.recipeapp.user.model.User;
import com.elena.recipeapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PantryItemService {
    
    private final PantryItemRepository pantryItemRepository;
    private final PantryItemMapper pantryItemMapper;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public PantryItemResponseDTO add(Long userId, PantryItemRequestDTO dto) {
        log.info("Adding item for user with id: {}", userId);

        // Prevent adding the same ingredient twice
        if (pantryItemRepository.existsByUserIdAndIngredientId(userId, dto.getIngredientId())) {
            log.warn("Ingredient id: {} already in pantry for user id: {}", dto.getIngredientId(), userId);
            throw new RuntimeException("Ingredient already exists in pantry");
        }

        User user = findUserById(userId);
        Ingredient ingredient = findIngredientById(dto.getIngredientId());

        PantryItem pantryItem = pantryItemMapper.toEntity(dto);
        pantryItem.setUser(user);
        pantryItem.setIngredient(ingredient);

        PantryItem saved = pantryItemRepository.save(pantryItem);
        log.debug("Pantry item saved with id: {}", saved.getId());

        return pantryItemMapper.toDto(saved);
    }

    public List<PantryItemResponseDTO> getAllForUser(Long userId) {
        log.info("Fetching all pantry items for user id: {}", userId);

        return  pantryItemRepository.findByUserId(userId)
                                    .stream()
                                    .map(pantryItemMapper :: toDto)
                                    .toList();
    }

    //Pantry items sorted by category name ascending
    public List<PantryItemResponseDTO> getAllForUserSortedByCategory(Long userId) {
        log.info("Fetching all pantry items sorted by category for user id: {}", userId);

        return  pantryItemRepository.findByUserIdOrderByIngredientCategoryNameAsc(userId)
                                    .stream()
                                    .map(pantryItemMapper :: toDto)
                                    .toList();
    }

    //Pantry items list filtered by category
    public List<PantryItemResponseDTO> getAllForUserByCategory(Long userId, Long categoryId) {
        log.info("Fetching all pantry items of category id: {} for user id: {}", categoryId, userId);

        return  pantryItemRepository.findByUserIdAndIngredientCategoryId(userId, categoryId)
                                    .stream()
                                    .map(pantryItemMapper :: toDto)
                                    .toList();
    }

    public PantryItemResponseDTO patch(Long userId, Long itemId, PantryItemPatchDTO dto) {
        log.info("Updating pantry item id: {} for user id: {}", itemId, userId);

        PantryItem pantryItem = findItemByIdAndUserId(itemId, userId);

        //Mapper has excluded this option so we set it here
        if(dto.getIngredientId() != null) {
            Ingredient ingredient = findIngredientById(dto.getIngredientId());
            pantryItem.setIngredient(ingredient);
        }

        //Mapper is handling the quantity & unit if not null
        pantryItemMapper.patchPantryItem(dto, pantryItem);

        PantryItem saved = pantryItemRepository.save(pantryItem);
        log.debug("Pantry item updated with id: {}", saved.getId());

        return pantryItemMapper.toDto(saved);
    }

    public void delete(Long userId, Long id) {
        log.info("Deleting pantry item with id: {}", id);

        PantryItem pantryItem = findItemByIdAndUserId(id, userId);
        pantryItemRepository.delete(pantryItem);

        log.debug("Pantry item deleted with id: {}", id);
    }

    //Helper methods

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with id: {}", id);
            return new RuntimeException("User not found with id: " + id);
        });
    }

    private Ingredient findIngredientById(Long id) {
        return ingredientRepository.findById(id).orElseThrow(() -> {
            log.warn("Ingredient not found with id: {}", id);
            return new RuntimeException("Ingredient not found with id: " + id);
        });
    }

    private PantryItem findItemByIdAndUserId(Long itemId, Long userId) {
        return pantryItemRepository.findByIdAndUserId(itemId, userId).orElseThrow(() -> {
            log.warn("Pantry item id: {} not found for user with id: {}", itemId, userId);
            return new RuntimeException("Pantry item not found or does not belong to user");
        });
    }
}
