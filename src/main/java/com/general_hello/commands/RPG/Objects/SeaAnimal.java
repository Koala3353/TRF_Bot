package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.Types.AttainableBy;
import com.general_hello.commands.RPG.Types.Rarity;

public class SeaAnimal extends Animal {
    private final Tool toolsToUse;

    public SeaAnimal(Rarity rarity, String name, String emoji, String description, Integer price) {
        super(rarity, name, emoji, AttainableBy.FISHING, description, price);
        Initializer.seaAnimalToId.put(name.toLowerCase().replace("'", "").replaceAll("\\s+", ""), this);
        this.toolsToUse = Initializer.toolToId.get("fishingpole");
        String[] split = name.split("\\s+");
        Initializer.seaAnimalToId.put(RPGDataUtils.filter(split[0]), this);
        Initializer.seaAnimals.add(this);
        Initializer.allItems.put(RPGDataUtils.filter(name), this);
        Initializer.allItems.put(RPGDataUtils.filter(split[0]), this);
        Initializer.allNames.add(name);
    }

    public SeaAnimal(Rarity rarity, String name, String emoji) {
        super(rarity, name, emoji, AttainableBy.FISHING, "This item's purpose is to be fished and collected or sold. Nothing more, nothing less.", null);
        Initializer.seaAnimalToId.put(name.toLowerCase().replace("'", "").replaceAll("\\s+", ""), this);
        this.toolsToUse = Initializer.toolToId.get("fishingpole");
        String[] split = name.split("\\s+");
        Initializer.seaAnimalToId.put(RPGDataUtils.filter(split[0]), this);
        Initializer.seaAnimals.add(this);
        Initializer.allItems.put(RPGDataUtils.filter(name), this);
        Initializer.allItems.put(RPGDataUtils.filter(split[0]), this);
        Initializer.allNames.add(name);
    }

    public Tool getToolsToUse() {
        return toolsToUse;
    }
}
