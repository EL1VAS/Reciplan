package com.elena.recipeapp.common.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.elena.recipeapp.enums.MatchStatus;
import com.elena.recipeapp.enums.Unit;
import com.elena.recipeapp.pantry.model.PantryItem;
import com.elena.recipeapp.recipe.model.RecipeIngredients;

public class PantryMatcherUtil {

    // Convert quantity to base unit
    private static double toBaseUnit(double quantity, Unit unit) {
        return unit.toBase(quantity);
    }

    // Ensure we got enough of the recipe ingredient in our pantry
    public static boolean hasEnough(PantryItem pantryItem, RecipeIngredients recipeIngredient) {
        Unit pantryUnit = pantryItem.getUnit();
        Unit recipeUnit = recipeIngredient.getUnit();

        if(!pantryUnit.isCompatibleWith(recipeUnit)) {
            return false;   
        }

        double pantryBaseQty = toBaseUnit(pantryItem.getQuantity(), pantryUnit);
        double recipeBaseQty = toBaseUnit(recipeIngredient.getQuantity().doubleValue(), recipeUnit);
        return pantryBaseQty >= recipeBaseQty;
    }

    public static double missingQuantity(PantryItem pantryItem, RecipeIngredients recipeIngredient) {
        Unit pantryUnit = pantryItem.getUnit();
        Unit recipeUnit = recipeIngredient.getUnit();

        if(!pantryUnit.isCompatibleWith(recipeUnit)) {
            return recipeIngredient.getQuantity().doubleValue();   // Incompatible units , so we need the whole quntity of the recipe ingredient
        }

        double pantryBaseQty = toBaseUnit(pantryItem.getQuantity(), pantryUnit);
        double recipeBaseQty = toBaseUnit(recipeIngredient.getQuantity().doubleValue(), recipeUnit);
        double missingBaseQty = recipeBaseQty - pantryBaseQty;
        if(missingBaseQty <= 0) {
            return 0;   // We have enough or more than needed
        }
        // Convert missing quantity back to recipe unit
        return missingBaseQty / recipeUnit.getToBaseUnit();
    }

    public static MatchStatus calculateMatchStatus (List<PantryItem> pantryItems, List<RecipeIngredients> recipeIngredients) {

        Map<Long, PantryItem> pantryMap = pantryItems.stream()
                .collect(Collectors.toMap(pi -> pi.getIngredient().getId(), pi -> pi));
                
        long fullyMatched = recipeIngredients.stream()
                .filter(ri -> {
                    PantryItem pantryItem = pantryMap.get(ri.getIngredient().getId());
                    return pantryItem != null && hasEnough(pantryItem, ri);
                })
                .count();

         if(fullyMatched == recipeIngredients.size())  return MatchStatus.FULL_MATCH;
         if(fullyMatched > 0)   return MatchStatus.PARTIAL_MATCH;
         return MatchStatus.NO_MATCH;
    }
   
}
