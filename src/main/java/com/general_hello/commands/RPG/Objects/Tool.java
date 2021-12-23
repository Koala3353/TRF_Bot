package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.Types.AttainableBy;
import com.general_hello.commands.RPG.Types.Rarity;

public class Tool extends Objects {
    private final AttainableBy toBeUsedOn;
    private final AttainableBy retrieveBy;

    public Tool(Rarity rarity, String name, AttainableBy retrieveBy, String emoji, String description) {
        super(name, rarity, 30_000, emoji, description);
        this.toBeUsedOn = AttainableBy.CRAFTING;
        this.retrieveBy = retrieveBy;
        Initializer.tools.add(this);
        String[] split = name.split("\\s+");
        Initializer.toolToId.put(RPGDataUtils.filter(split[0]), this);
        Initializer.allNames.add(name);
        Initializer.allItems.put(RPGDataUtils.filter(name), this);
        Initializer.allItems.put(RPGDataUtils.filter(split[0]), this);
        Initializer.toolToId.put(name.toLowerCase().replace("'", "").replaceAll("\\s+", ""), this);
    }

    public AttainableBy getRetrieveBy() {
        return retrieveBy;
    }

    public AttainableBy getToBeUsedOn() {
        return toBeUsedOn;
    }
}
