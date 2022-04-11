package com.general_hello.commands.Objects;

import com.general_hello.commands.Items.Initializer;

public class Skill extends Object {
    private final int damage;
    private final int heal;

    public Skill(String name, Integer costToBuy, String emojiOfSkill, String description, int damage, int heal) {
        super(name, costToBuy, emojiOfSkill, description);
        this.damage = damage;
        this.heal = heal;
        Initializer.skills.add(this);
        Initializer.nameToSkill.put(name, this);
    }

    public int getDamage() {
        return damage;
    }

    public int getHeal() {
        return heal;
    }
 }
