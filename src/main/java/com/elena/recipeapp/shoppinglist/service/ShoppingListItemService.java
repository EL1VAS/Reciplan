package com.elena.recipeapp.shoppinglist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elena.recipeapp.ingredient.model.Ingredient;
import com.elena.recipeapp.ingredient.repository.IngredientRepository;
import com.elena.recipeapp.pantry.dto.PantryItemPatchDTO;
import com.elena.recipeapp.pantry.dto.PantryItemResponseDTO;
import com.elena.recipeapp.pantry.model.PantryItem;
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
public class ShoppingListItemService {
    
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ShoppingListItemMapper shoppingListItemMapper;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public ShoppingListItemResponseDTO add(Long userId, ShoppingListItemRequestDTO dto) {
        log.info("Adding item for user with id: {}", userId);

        // Prevent adding the same ingredient twice
        if (shoppingListItemRepository.existsByUserIdAndIngredientId(userId, dto.getIngredientId())) {
            log.warn("Ingredient id: {} already in pantry for user id: {}", dto.getIngredientId(), userId);
            throw new RuntimeException("Ingredient already exists in pantry");
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

    public List<ShoppingListItemResponseDTO> getAllForUser(Long userId) {
        log.info("Fetching all shopping list items for user id: {}", userId);

        return  shoppingListItemRepository.findByUserId(userId)
                                    .stream()
                                    .map(shoppingListItemMapper :: toDto)
                                    .toList();
    }

    public List<ShoppingListItemResponseDTO> getAllForUserSortedByCategory(Long userId) {
        log.info("Fetching all shopping list items sorted by category for user id: {}", userId);

        return  shoppingListItemRepository.findByUserIdOrderByIngredientCategoryNameAsc(userId)
                                    .stream()
                                    .map(shoppingListItemMapper :: toDto)
                                    .toList();
    }

    public List<ShoppingListItemResponseDTO> getUncheckedForUser(Long userId) {
        log.info("Fetching all unchecked shopping list items for user id: {}", userId);

        return  shoppingListItemRepository.findByUserIdAndChecked(userId, false)
                                    .stream()
                                    .map(shoppingListItemMapper :: toDto)
                                    .toList();
    }

    public ShoppingListItemResponseDTO patch(Long userId, Long id, ShoppingListItemPatchDTO dto) {
        log.info("Updating pantry item id: {} for user id: {}", id, userId);

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

    public void delete(Long userId, Long id) {
        log.info("Deleting shopping list item id: {} of user id: {}", id, userId);

        ShoppingListItem item = findItemByIdAndUserId(id, userId);
        shoppingListItemRepository.delete(item);

        log.debug("Deleted shopping list item id: {}", id);
    }

    public void clearAll(Long userId) {
        log.info("Deleting shopping list of user id: {}", userId);

        shoppingListItemRepository.deleteByUserId(userId);
        log.debug("Shopping list deleted");
    }

    public void clearChecked(Long userId) {
        log.info("Clearing checked items for user id: {}", userId);
        
        List<ShoppingListItem> checkedItems = shoppingListItemRepository.findByUserIdAndChecked(userId, true);
        shoppingListItemRepository.deleteAll(checkedItems);
        
        log.debug("Cleared {} checked items for user id: {}", checkedItems.size(), userId);
    }

    // Convenience method — just toggle checked without sending full patch
    public ShoppingListItemResponseDTO toggleChecked(Long userId, Long itemId) {
        log.info("Toggling checked for shopping list item id: {} for user id: {}", itemId, userId);

        ShoppingListItem item = findItemByIdAndUserId(itemId, userId);
        item.setChecked(!item.isChecked());  // flip true→false or false→true

        ShoppingListItem saved = shoppingListItemRepository.save(item);
        log.debug("Item id: {} checked status is now: {}", saved.getId(), saved.isChecked());
        return shoppingListItemMapper.toDto(saved);
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

    private ShoppingListItem findItemByIdAndUserId(Long itemId, Long userId) {
        return shoppingListItemRepository.findByIdAndUserId(itemId, userId).orElseThrow(() -> {
            log.warn("Shopping list item id: {} not found for user with id: {}", itemId, userId);
            return new RuntimeException("Shopping list item not found or does not belong to user");
        });
    }
}

