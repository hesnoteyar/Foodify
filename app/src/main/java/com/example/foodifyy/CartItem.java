package com.example.foodifyy;
import java.io.Serializable;


public class CartItem implements Serializable{
    private String foodName;
    private int quantity;
    private double amount;

    // Required default constructor for Firebase
    public CartItem() {
    }

    public CartItem(String foodName, int quantity, double amount) {
        this.foodName = foodName;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmount() {
        return amount;
    }
}

