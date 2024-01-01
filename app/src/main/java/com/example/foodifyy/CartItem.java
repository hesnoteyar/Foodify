package com.example.foodifyy;

public class CartItem {
    private String itemName;
    private int quantity;
    private double totalAmount;

    public CartItem(String itemName, int quantity, double totalAmount) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    // Implement getter methods for itemName, quantity, and totalAmount
    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

