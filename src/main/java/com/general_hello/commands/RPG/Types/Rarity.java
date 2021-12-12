package com.general_hello.commands.RPG.Types;

public enum Rarity {
    COMMON(1, "Common"),
    UNCOMMON(2, "Uncommon"),
    RARE(3, "Rare"),
    LEGENDARY(4, "Legendary"),
    MYTHICAL(5, "Mythical");
    private final int multiplier;
    private final String name;

    Rarity(int multiplier, String name) {
        this.multiplier = multiplier;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getMultipliedValue(int valueToBeMultiplied) {
        return valueToBeMultiplied * getMultiplier();
    }

    public int getWorth() {
        StringBuilder multiplier = new StringBuilder(String.valueOf(getMultiplier()));
        int x = getMultiplier();
        int y = 1;
        while (y < x) {
            multiplier.append(0);
            y++;
        }

        return Integer.parseInt(multiplier.toString());
    }

    public int getMultipliedValue() {
        return 10 * getMultiplier();
    }
}
