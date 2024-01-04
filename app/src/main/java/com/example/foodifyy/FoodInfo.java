package com.example.foodifyy;

public class FoodInfo {
    private double fats;
    private double protein;
    private double calories;

    public FoodInfo(double fats, double protein, double calories) {
        this.fats = fats;
        this.protein = protein;
        this.calories = calories;
    }

    public double getFats() {
        return fats;
    }

    public double getProtein() {
        return protein;
    }

    public double getCalories() {
        return calories;
    }
}

