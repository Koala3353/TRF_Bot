package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.Types.Rarity;
import com.general_hello.commands.commands.Utils.UtilNum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chest extends Objects {
    private final List<String> itemsInChest;
    private final int shekelsMin;
    private final int shekelsMax;

    public Chest(String name, Rarity rarity, String emojiOfItem, int shekelsMin, int shekelsMax, String... items) {
        super(name, rarity, null, emojiOfItem, "A chest filled with items based on the chests rarity!");
        itemsInChest = Arrays.asList(items);
        this.shekelsMin = shekelsMin;
        this.shekelsMax = shekelsMax;

        Initializer.allItems.put(RPGDataUtils.filter(name), this);
        Initializer.chests.add(this);
        Initializer.chestToId.put(RPGDataUtils.filter(name), this);
    }

    public List<Objects> getItemsInChest() {
        return convertStringToObjects(this.itemsInChest);
    }

    public List<Objects> convertStringToObjects(List<String> string) {
        int x = 0;
        List<Objects> objects = new ArrayList<>();
        while (x < string.size()) {
            objects.add(Initializer.allItems.get(RPGDataUtils.filter(string.get(x))));
            x++;
        }
        return objects;
    }

    public List<Objects> getRandomItems() {
        List<String> randomItems = new ArrayList<>();
        do {
            int randomNum = UtilNum.randomNum(0, itemsInChest.size()-1);
            randomItems.add(itemsInChest.get(randomNum));

        } while (UtilNum.randomNum(0, 3) >= 1);
        return convertStringToObjects(randomItems);
    }

    public int getShekelsMin() {
        return shekelsMin;
    }

    public int getShekelsMax() {
        return shekelsMax;
    }

    public int getRandomShekels() {
        return UtilNum.randomNum(getShekelsMin(), getShekelsMax());
    }
}
