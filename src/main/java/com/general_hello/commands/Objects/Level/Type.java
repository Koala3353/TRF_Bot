package com.general_hello.commands.Objects.Level;

public enum Type {
    PS("Playstation"),
    PC("Personal Computer");

    // Variables of the object
    private final String name;

    // Initializer
    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
