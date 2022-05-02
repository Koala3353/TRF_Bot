package com.general_hello.commands.Objects.Items;

import com.general_hello.commands.Objects.User.Rank;

public class LimitedItems extends Item {
    // Variables
    private final int maxStock;
    private int currentStock;

    // Initializers
    public LimitedItems(String name, Integer costToBuy, String emojiOfItem, String description, Rank rank, boolean patreonOnly, int maxStock, int currentStock, boolean premium) {
        super(name, costToBuy, emojiOfItem, description, rank, premium, patreonOnly);
        this.maxStock = maxStock;
        this.currentStock = currentStock;
    }

    // Getters and setters
    public int getMaxStock() {
        return maxStock;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public LimitedItems setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
        return this;
    }
}
