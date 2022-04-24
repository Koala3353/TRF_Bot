package com.general_hello.commands.Objects;

import com.general_hello.commands.Items.Initializer;

public enum Achievement {
    NEW_ACCOUNT("New Account", "When players just started the game/created the account successfully (R1, Newbie)");
    private final String name;
    private final String description;

    Achievement(String name, String description) {
        this.name = name;
        this.description = description;
        Initializer.achievements.add(this);
        Initializer.allAchievementNames.add(name);
        Initializer.allAchievements.put(name, this);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
