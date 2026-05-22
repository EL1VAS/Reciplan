package com.elena.recipeapp.recipe.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elena.recipeapp.common.exceptions.ResourceNotFoundException;
import com.elena.recipeapp.common.exceptions.UnauthorizedException;
import com.elena.recipeapp.common.util.PantryMatcherUtil;
import com.elena.recipeapp.enums.Difficulty;
import com.elena.recipeapp.enums.MatchStatus;
import com.elena.recipeapp.ingredient.model.Ingredient;
import com.elena.recipeapp.ingredient.repository.IngredientRepository;
import com.elena.recipeapp.pantry.repository.PantryItemRepository;
import com.elena.recipeapp.recipe.dto.RecipeIngredientRequestDTO;
import com.elena.recipeapp.recipe.dto.RecipeMatchResponseDTO;
import com.elena.recipeapp.recipe.dto.RecipeListResponseDTO;
import com.elena.recipeapp.recipe.dto.RecipePatchDTO;
import com.elena.recipeapp.recipe.dto.RecipeRequestDTO;
import com.elena.recipeapp.recipe.dto.RecipeResponseDTO;
import com.elena.recipeapp.recipe.mapper.RecipeMapper;
import com.elena.recipeapp.recipe.model.Recipe;
import com.elena.recipeapp.recipe.model.RecipeIngredients;
import com.elena.recipeapp.recipe.repository.RecipeIngredientsRepository;
import com.elena.recipeapp.recipe.repository.RecipeRepository;
import com.elena.recipeapp.recipe.specification.RecipeSpecification;
import com.elena.recipeapp.user.model.User;
import com.elena.recipeapp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {
    
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final PantryItemRepository pantryItemRepository;

    @Transactional
    public RecipeResponseDTO create(Long userId, RecipeRequestDTO dto) {
        log.info("Creating recipe: {} by user id: {}", dto.getTitle(), userId);

        User user = findUserById(userId);
        //Set the createdBy
        Recipe recipe = recipeMapper.toEntity(dto);
        recipe.setCreatedBy(user);

        //Build recipe ingredients list
        List<RecipeIngredients> recipeIngredients = buildIngredientLines(dto.getRecipeIngredients(), recipe);
        recipe.setRecipeIngredients(recipeIngredients);

        Recipe saved = recipeRepository.save(recipe);
        log.debug("Created new recipe with id: {}", saved.getId());

        return recipeMapper.toDto(saved);
    }

    public Page<RecipeListResponseDTO> getAll(
        String title,
        Difficulty difficulty,
        Integer maxPrepTime,
        Boolean vegetarian,
        Boolean lactoseFree,
        Boolean glutenFree,
        Pageable pageable) {
            log.info("Fetching recipes with filters");

            Specification<Recipe> spec = Specification.where(null);

            if(title != null)   spec = spec.and(RecipeSpecification.hasTitle(title));
            if(difficulty != null)   spec = spec.and(RecipeSpecification.hasDifficulty(difficulty));
            if(maxPrepTime != null)   spec = spec.and(RecipeSpecification.maxPrepTime(maxPrepTime));
            if(Boolean.TRUE.equals(vegetarian))   spec = spec.and(RecipeSpecification.isVegetarian());
            if(Boolean.TRUE.equals(lactoseFree))   spec = spec.and(RecipeSpecification.isLactoseFree());
            if(Boolean.TRUE.equals(glutenFree))   spec = spec.and(RecipeSpecification.isGlutenFree());

            return recipeRepository.findAll(spec, pageable).map(recipeMapper :: toListDto);
    }

    public RecipeResponseDTO getById(Long id) {
        log.info("Fetching recipe with id: {}", id);

        Recipe recipe = findRecipeById(id);
        recipe.setRecipeIngredients(recipeIngredientsRepository.findByRecipeId(id));
        return recipeMapper.toDto(recipe);
    }

    public Page<RecipeListResponseDTO> getByIngredient(Long ingredientId, Pageable pageable) {
        log.info("Fetching recipes that include ingredient with id: {}", ingredientId);
        return recipeRepository.findByRecipeIngredientsIngredientId(ingredientId, pageable).map(recipeMapper :: toListDto);
    }
 
    @Transactional
    public RecipeListResponseDTO patch(Long userId, Long id, RecipePatchDTO dto) {
        log.info("Updating recipe with id: {}", id);

        Recipe recipe = findRecipeById(id);
        checkOwnership(recipe, userId);

        recipeMapper.patchRecipe(dto, recipe);

        //If ingredient list is present delete the whole list
        if(dto.getRecipeIngredients() != null && !dto.getRecipeIngredients().isEmpty()) {
            recipe.getRecipeIngredients().clear();
            //And create new list 
            List<RecipeIngredients> newIngredients = buildIngredientLines(dto.getRecipeIngredients(), recipe);
            recipe.getRecipeIngredients().addAll(newIngredients);
        }

        
        Recipe saved = recipeRepository.save(recipe);
        log.debug("Recipe saved with id: {}", saved.getId());

        return recipeMapper.toListDto(saved);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        log.info("Deleting recipe id: {}", id);
        Recipe recipe = findRecipeById(id);
        checkOwnership(recipe, userId);
        recipeRepository.deleteById(id);
        log.debug("Deleted recipe with id: {} created by user id: {}", id, userId);
    }

    
    public Page<RecipeMatchResponseDTO> getMatchesForUser(Long userId, Pageable pageable) {
        log.info("Fetching recipe matches for user id: {}", userId);

        //Get pantry ingredient ids
        Set<Long> pantryIds = pantryItemRepository.findIngredientIdsByUserId(userId);

        //Score and filter recipes then paginate manually

        List<RecipeMatchResponseDTO> matches = recipeRepository.findAllWithIngredients()
                .stream()
                .map(recipe -> scoreRecipe(recipe, pantryIds))
                .filter(match -> match.getMatchStatus() != MatchStatus.NO_MATCH)
                .sorted(Comparator.comparing(match -> match.getMatchStatus().ordinal()))
                .toList();

        //Manual pagination slice
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), matches.size());
        List<RecipeMatchResponseDTO> pageContent = start >= matches.size() 
                ? List.of() 
                : matches.subList(start, end);
        return new PageImpl<>(pageContent, pageable, matches.size());
    } 



    //Helper methods
    private Recipe findRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> {
            log.warn("Recipe not found with id: {}", id);
            return new ResourceNotFoundException("Recipe not found with id: " + id);
        });
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User not found with id: {}", userId);
            return new ResourceNotFoundException("User not found with id: " + userId);
        });
    }

    private void checkOwnership(Recipe recipe, Long userId) {
        if (!recipe.getCreatedBy().getId().equals(userId)) {
            log.warn("User id: {} attempted to modify recipe id: {} they don't own", userId, recipe.getId());
            throw new UnauthorizedException("You are not authorized to modify this recipe");
        }
    }

    private List<RecipeIngredients> buildIngredientLines (List<RecipeIngredientRequestDTO> dtos, Recipe recipe) {
        
        List<Long> ingredientIds = dtos.stream()
                                    .map(RecipeIngredientRequestDTO::getIngredientId)
                                    .toList();

        Map<Long, Ingredient> ingredientMap = ingredientRepository.findAllById(ingredientIds)
                                    .stream()
                                    .collect(Collectors.toMap(Ingredient::getId, ingredient -> ingredient));

        return dtos.stream()
                .map(dto -> {
                    Ingredient ingredient = ingredientMap.get(dto.getIngredientId());
                    if(ingredient == null) {
                        log.warn("Ingredient not found with id: {}", dto.getIngredientId());
                        throw new ResourceNotFoundException("Ingredient not found with id: " + dto.getIngredientId());
                    }
                    return RecipeIngredients.builder()
                            .recipe(recipe)
                            .ingredient(ingredient)
                            .quantity(dto.getQuantity())
                            .unit(dto.getUnit())
                            .build();
            }).toList();
    }  

    private RecipeMatchResponseDTO scoreRecipe(Recipe recipe, Set<Long> pantryIds) {

        List<Long> recipeIngredientIds = recipe.getRecipeIngredients()
                    .stream()
                    .map(ri -> ri.getIngredient().getId())
                    .toList();

        MatchStatus status = PantryMatcherUtil.calculateMatchStatus(pantryIds, recipeIngredientIds);

        List<Long> missingIds = PantryMatcherUtil.getMissingIngredientIds(pantryIds, recipeIngredientIds);

        return RecipeMatchResponseDTO.builder()
                    .recipe(recipeMapper.toListDto(recipe))
                    .matchStatus(status)
                    .missingIngredientIds(missingIds)
                    .build();
    }

}
