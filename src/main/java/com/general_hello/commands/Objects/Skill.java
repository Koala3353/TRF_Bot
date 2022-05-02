package com.general_hello.commands.Objects;

import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.User.Rank;

import java.util.ArrayList;
import java.util.Arrays;

public class Skill extends Object {
    // Skill object that extends Object
    // Variables
    private final int energy;
    private final ArrayList<EffectTypes> effects;
    private final boolean neodevilfruitonly;
    private final boolean premium;

    // Initializer
    public Skill(String name, Integer costToBuy, String emoji, String description, Rank rank, boolean patreonOnly, int energy, boolean neodevilfruitonly, boolean premium, EffectTypes... effects) {
        super(name, costToBuy, emoji, description, rank, patreonOnly);
        this.energy = energy;
        this.effects = new ArrayList<>(Arrays.asList(effects));
        this.neodevilfruitonly = neodevilfruitonly;
        this.premium = premium;
        Initializer.nameToSkill.put(name, this);
        Initializer.skills.add(this);
    }

    // Getters
    public int getEnergy() {
        return energy;
    }

    public ArrayList<EffectTypes> getEffects() {
        return effects;
    }

    public boolean isPremium() {
        return premium;
    }

    public String effectsString() {
        ArrayList<EffectTypes> effects = this.effects;
        StringBuilder string = new StringBuilder();
        int x = 0;
        for (EffectTypes effectTypes : effects) {
            string.append(effectTypes.getName());
            x++;
            if (x != effects.size()) {
                string.append(", ");
            }
        }

        return string.toString();
    }

    public boolean isNeodevilfruitonly() {
        return neodevilfruitonly;
    }
}
