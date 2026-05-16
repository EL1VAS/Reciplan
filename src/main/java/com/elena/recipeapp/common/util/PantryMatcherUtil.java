package com.elena.recipeapp.common.util;

import java.util.List;
import java.util.Set;

import com.elena.recipeapp.enums.MatchStatus;

public class PantryMatcherUtil {

    public static MatchStatus calculateMatchStatus (Set<Long> pantryIngredientIds, List<Long> recipeIngredientIds) {
        long matched = recipeIngredientIds.stream()
                .filter(pantryIngredientIds :: contains)
                .count();

        if(matched == recipeIngredientIds.size())  return MatchStatus.FULL_MATCH;
        if(matched > 0)   return MatchStatus.PARTIAL_MATCH;
        return MatchStatus.NO_MATCH;
    }

    public static List<Long> getMissingIngredientIds(Set<Long> pantryIngredientIds, List<Long> recipeIngredientIds) {
        return recipeIngredientIds.stream().filter(id -> !pantryIngredientIds.contains(id)).toList();
    }   
}
