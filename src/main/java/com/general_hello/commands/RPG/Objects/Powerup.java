package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.Types.Rarity;

public class Powerup extends Objects {
    public Powerup(String name, Rarity rarity, Integer costToBuy, String emojiOfItem, String description) {
        super(name, rarity, costToBuy, emojiOfItem, description);
        String[] split = name.split("\\s+");
        Initializer.powerUpToId.put(RPGDataUtils.filter(split[0]), this);
        Initializer.allItems.put(RPGDataUtils.filter(name), this);
        Initializer.allItems.put(RPGDataUtils.filter(split[0]), this);
        Initializer.powerUps.add(this);
        Initializer.powerUpToId.put(RPGDataUtils.filter(name), this);
    }
}
