package com.general_hello.commands.Objects;

import com.general_hello.commands.Items.Initializer;

public class Item extends Object {
    public Item(String name, Integer costToBuy, String emojiOfItem, String description) {
        super(name, costToBuy, emojiOfItem, description);
        Initializer.nameToItem.put(name, this);
        Initializer.items.add(this);
    }
 }
