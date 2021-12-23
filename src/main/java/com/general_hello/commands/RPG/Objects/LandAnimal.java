package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.Types.AttainableBy;
import com.general_hello.commands.RPG.Types.Rarity;

public class LandAnimal extends Animal {
    private final Tool toolsToUse;

    public LandAnimal(Rarity rarity, String name, String emoji, String description, int price) {
        super(rarity, name, emoji, AttainableBy.HUNTING, description, price);
        this.toolsToUse = Initializer.toolToId.get("huntingrifle");
        String[] split = name.split("\\s+");
        Initializer.landAnimalToId.put(RPGDataUtils.filter(split[0]), this);
        Initializer.allItems.put(RPGDataUtils.filter(name), this);
        Initializer.allItems.put(RPGDataUtils.filter(split[0]), this);
        Initializer.landAnimalToId.put(name.toLowerCase().replace("'", "").replaceAll("\\s+", ""), this);
        Initializer.landAnimals.add(this);
        Initializer.allNames.add(name);
    }

    public LandAnimal(Rarity rarity, String name, String emoji) {
        super(rarity, name, emoji, AttainableBy.HUNTING, "Meet " + name + ". It is just a " + name + ". Nothing more, nothing less.", null);
        this.toolsToUse = Initializer.toolToId.get("huntingrifle");
        String[] split = name.split("\\s+");
        Initializer.allItems.put(RPGDataUtils.filter(name), this);
        Initializer.allItems.put(RPGDataUtils.filter(split[0]), this);
        Initializer.landAnimalToId.put(RPGDataUtils.filter(split[0]), this);
        Initializer.landAnimalToId.put(name.toLowerCase().replace("'", "").replaceAll("\\s+", ""), this);
        Initializer.landAnimals.add(this);
        Initializer.allNames.add(name);
    }

    public Tool getToolsToUse() {
        return toolsToUse;
    }
}
