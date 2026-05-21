package com.elena.recipeapp.shoppinglist.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elena.recipeapp.common.exceptions.DuplicateResourceException;
import com.elena.recipeapp.common.exceptions.ResourceNotFoundException;
import com.elena.recipeapp.ingredient.model.Ingredient;
import com.elena.recipeapp.ingredient.repository.IngredientRepository;
import com.elena.recipeapp.pantry.repository.PantryItemRepository;
import com.elena.recipeapp.recipe.model.Recipe;
import com.elena.recipeapp.recipe.model.RecipeIngredients;
import com.elena.recipeapp.recipe.repository.RecipeRepository;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemPatchDTO;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemRequestDTO;
import com.elena.recipeapp.shoppinglist.dto.ShoppingListItemResponseDTO;
import com.elena.recipeapp.shoppinglist.mapper.ShoppingListItemMapper;
import com.elena.recipeapp.shoppinglist.model.ShoppingListItem;
import com.elena.recipeapp.shoppinglist.repository.ShoppingListItemRepository;
import com.elena.recipeapp.user.model.User;
import com.elena.recipeapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingListItemService {
    
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ShoppingListItemMapper shoppingListItemMapper;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final PantryItemRepository pantryItemRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public ShoppingListItemResponseDTO add(Long userId, ShoppingListItemRequestDTO dto) {
        log.info("Adding item for user with id: {}", userId);

        // Prevent adding the same ingredient twice
        if (shoppingListItemRepository.existsByUserIdAndIngredientId(userId, dto.getIngredientId())) {
            log.warn("Ingredient id: {} already in shopping list for user id: {}", dto.getIngredientId(), userId);
            throw new DuplicateResourceException("Ingredient already exists in shopping list");
        }

        User user = findUserById(userId);
        Ingredient ingredient = findIngredientById(dto.getIngredientId());

        ShoppingListItem item = shoppingListItemMapper.toEntity(dto);
        item.setUser(user);
        item.setIngredient(ingredient);

        ShoppingListItem saved = shoppingListItemRepository.save(item);
        log.debug("Shopping list item saved with id: {}", saved.getId());
        return shoppingListItemMapper.toDto(saved);
    }

    public Page<ShoppingListItemResponseDTO> getAllForUser(Long userId, Pageable pageable) {
        log.info("Fetching all shopping list items for user id: {}", userId);

        return  shoppingListItemRepository.findByUserId(userId, pageable)
                                    .map(shoppingListItemMapper :: toDto);
    }

    public Page<ShoppingListItemResponseDTO> getAllForUserSortedByCategory(Long userId, Pageable pageable) {
        log.info("Fetching all shopping list items sorted by category for user id: {}", userId);

        return  shoppingListItemRepository.findByUserIdOrderByIngredientCategoryNameAsc(userId, pageable)
                                    .map(shoppingListItemMapper :: toDto);
    }

    public List<ShoppingListItemResponseDTO> getUncheckedForUser(Long userId) {
        log.info("Fetching all unchecked shopping list items for user id: {}", userId);

        return  shoppingListItemRepository.findByUserIdAndChecked(userId, false)
                                    .stream()
                                    .map(shoppingListItemMapper :: toDto)
                                    .toList();
    }

    
    @Transactional
    public ShoppingListItemResponseDTO patch(Long userId, Long id, ShoppingListItemPatchDTO dto) {
        log.info("Updating shopping list item id: {} for user id: {}", id, userId);

        ShoppingListItem item = findItemByIdAndUserId(id, userId);

        //Mapper has excluded this option so we set it here
        if(dto.getIngredientId() != null) {
            Ingredient ingredient = findIngredientById(dto.getIngredientId());
            item.setIngredient(ingredient);
        }

        //Mapper is handling the quantity & unit if not null
        shoppingListItemMapper.patchShoppingListItem(dto, item);

        ShoppingListItem saved = shoppingListItemRepository.save(item);
        log.debug("Shopping list item updated with id: {}", saved.getId());

        return shoppingListItemMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        log.info("Deleting shopping list item id: {} of user id: {}", id, userId);

        ShoppingListItem item = findItemByIdAndUserId(id, userId);
        shoppingListItemRepository.delete(item);

        log.debug("Deleted shopping list item id: {}", id);
    }

    @Transactional
    public void clearAll(Long userId) {
        log.info("Deleting shopping list of user id: {}", userId);

        shoppingListItemRepository.deleteByUserId(userId);
        log.debug("Shopping list deleted");
    }

    @Transactional
    public void clearChecked(Long userId) {
        log.info("Clearing checked items for user id: {}", userId);
        
        List<ShoppingListItem> checkedItems = shoppingListItemRepository.findByUserIdAndChecked(userId, true);
        shoppingListItemRepository.deleteAll(checkedItems);

        log.debug("Cleared {} checked items for user id: {}", checkedItems.size(), userId);
    }

    @Transactional
    public void addMissingIngredientsFromRecipe(Long userId, Long recipeId) {
        log.info("Adding missing ingredients from recipe id: {} to shopping list for user id: {}", recipeId, userId);

        //Get user's pantry ids
        Set<Long> pantryIds = pantryItemRepository.findIngredientIdsByUserId(userId);

        //Get recipe and check existence    
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> {
            log.warn("Recipe not found with id: {}", recipeId);
            return new ResourceNotFoundException("Recipe not found with id: " + recipeId);
        });

        //Fetch user
        User user = findUserById(userId);

        //Find missing ingredients and add to shopping list
        for (RecipeIngredients ri : recipe.getRecipeIngredients()) {
            Long ingredientId = ri.getIngredient().getId();

            if(pantryIds.contains(ingredientId)) {
                log.debug("Ingredient id: {} already in pantry, skipping", ingredientId);
                continue;
            }

            if(shoppingListItemRepository.existsByUserIdAndIngredientId(userId, ingredientId)) {
                log.debug("Ingredient id: {} already in shopping list, skipping", ingredientId);
                continue;
            }

            ShoppingListItem item = ShoppingListItem.builder()
                        .user(user)
                        .ingredient(ri.getIngredient())
                        .checked(false)
                        .build();
                
            shoppingListItemRepository.save(item);
            log.debug("Added ingredient id: {} to shopping list for user id: {}", ingredientId, userId);    
            }
        log.debug("Finished adding missing ingredients from recipe id: {}", recipeId);
    }

    // Convenience method — just toggle checked without sending full patch
    @Transactional
    public ShoppingListItemResponseDTO toggleChecked(Long userId, Long id) {
        log.info("Toggling checked for shopping list item id: {} for user id: {}", id, userId);

        ShoppingListItem item = findItemByIdAndUserId(id, userId);
        item.setChecked(!item.isChecked());  // flip true→false or false→true

        ShoppingListItem saved = shoppingListItemRepository.save(item);
        log.debug("Item id: {} checked status is now: {}", saved.getId(), saved.isChecked());
        return shoppingListItemMapper.toDto(saved);
    }




    //Helper methods
    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with id: {}", id);
            return new ResourceNotFoundException("User not found with id: " + id);
        });
    }

    private Ingredient findIngredientById(Long id) {
        return ingredientRepository.findById(id).orElseThrow(() -> {
            log.warn("Ingredient not found with id: {}", id);
            return new ResourceNotFoundException("Ingredient not found with id: " + id);
        });
    }

    private ShoppingListItem findItemByIdAndUserId(Long itemId, Long userId) {
        return shoppingListItemRepository.findByIdAndUserId(itemId, userId).orElseThrow(() -> {
            log.warn("Shopping list item id: {} not found for user with id: {}", itemId, userId);
            return new ResourceNotFoundException("Shopping list item not found or does not belong to user");
        });
    }
}

