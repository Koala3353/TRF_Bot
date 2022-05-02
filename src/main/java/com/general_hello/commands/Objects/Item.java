package com.general_hello.commands.Objects;

import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.User.Rank;

public class Item extends Object {
    // This class extends the Object
    private final boolean premium;
    // Initializer and adds it to the list
    public Item(String name, Integer costToBuy, String emojiOfItem, String description, Rank rank, boolean premium, boolean patreonOnly) {
        super(name, costToBuy, emojiOfItem, description, rank, patreonOnly);
        Initializer.nameToItem.put(name, this);
        this.premium = premium;
        Initializer.items.add(this);
    }

    // Getters
    public boolean isPremium() {
        return premium;
    }
}
