package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.Types.AttainableBy;
import com.general_hello.commands.RPG.Types.Rarity;

public class Animal extends Objects {
    private final int rewardForCooking;
    private final AttainableBy retrieveBy;

    public Animal(Rarity rarity, String name, String emojiOfItem, AttainableBy attainableBy, String description, Integer price) {
        super(name, rarity, price, emojiOfItem, description);
        this.rewardForCooking = rarity.getMultipliedValue();
        this.retrieveBy = attainableBy;
        Initializer.allAnimals.put(RPGDataUtils.filter(name), this);
        Initializer.animals.add(RPGDataUtils.filter(name));
    }

    public AttainableBy getRetrieveBy() {
        return retrieveBy;
    }

    public int getRewardForCooking() {
        return rewardForCooking;
    }
}
