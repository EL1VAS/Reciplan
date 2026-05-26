package com.elena.recipeapp.enums;

import lombok.Getter;

@Getter
public enum Unit {

    // Volume - base unit: ml
    ML("ml", "volume", 1.0),
    CL("cl", "volume", 10.0),
    DL("dl", "volume", 100.0),
    L("l", "volume", 1000.0),
    TSP("tsp", "volume", 4.92),
    TBSP("tbsp", "volume", 14.79),
    CUP("cup", "volume", 240.0),

    // Weight - base unit: grams
    G("g", "weight", 1.0),
    KG("kg", "weight", 1000.0),

    // Count - no conversion
    PCS("pcs", "count", 1.0);

    private final String label;
    private final String type;
    private final double toBaseUnit;

    Unit(String label, String type, double toBaseUnit) {
        this.label = label;
        this.type = type;
        this.toBaseUnit = toBaseUnit;
    }

    public double toBase(double quantity) {
        return quantity * toBaseUnit;
    }

    public boolean isCompatibleWith(Unit other) {
        return this.type.equals(other.type);
    }
    
}
