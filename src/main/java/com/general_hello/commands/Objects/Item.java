package com.general_hello.commands.Objects;

import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.User.Rank;

public class Item extends Object {
    public Item(String name, Integer costToBuy, String emojiOfItem, String description, Rank rank, boolean patreonOnly) {
        super(name, costToBuy, emojiOfItem, description, rank, patreonOnly);
        Initializer.nameToItem.put(name, this);
        Initializer.items.add(this);
    }
 }
