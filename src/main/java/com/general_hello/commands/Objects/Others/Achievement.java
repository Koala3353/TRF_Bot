package com.general_hello.commands.Objects.Others;

import com.general_hello.commands.Items.Initializer;

public enum Achievement {
    // Achievement initializer
    // Full documentation at Stage 4
    // Format: ACHIEVEMENT_NAME("STRING NAME"), (; if its the last entry)
    NEW_ACCOUNT("New Account"),
    REACH_R2("Player reaches R2"),
    REACH_R3("Player reaches R3"),
    REACH_R4("Player reaches R4"),
    REACH_R5("Player reaches R5"),
    REACH_R6("Player reaches R6"),
    REACH_R7("Player reaches R7"),
    REACH_R8("Player reaches R8");
    // Variable
    private final String name;

    // Initializer and adds it to the list
    Achievement(String name) {
        this.name = name;
        Initializer.achievements.add(this);
        Initializer.allAchievementNames.add(name);
        Initializer.allAchievements.put(name, this);
    }

    public String getName() {
        return name;
    }

}
