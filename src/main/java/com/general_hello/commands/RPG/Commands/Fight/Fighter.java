package com.general_hello.commands.RPG.Commands.Fight;

import com.general_hello.commands.Bot;
import net.dv8tion.jda.api.entities.User;

public class Fighter {
    private int health;
    private int shield;
    private final long user;

    public Fighter(User user) {
        this.health = 100;
        this.shield = 0;
        this.user = user.getIdLong();
    }

    public User getUser() {
        return Bot.jda.getUserById(user);
    }

    public int getHealth() {
        return health;
    }

    public Fighter setHealth(int health) {
        this.health = health;
        return this;
    }

    public int getShield() {
        return shield;
    }

    public Fighter setShield(int shield) {
        this.shield = shield;
        return this;
    }
}
