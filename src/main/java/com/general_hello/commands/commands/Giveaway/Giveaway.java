package com.general_hello.commands.commands.Giveaway;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class Giveaway {
    private final ArrayList<User> users;
    private final MessageChannel channel;
    private final long credits;
    private final OffsetDateTime length;
    private final Role requirement;
    private final String message;
    private final User starter;

    public Giveaway(MessageChannel channel, long credits, OffsetDateTime length, Role requirement, String message, User starter) {
        this.channel = channel;
        this.credits = credits;
        this.length = length;
        this.requirement = requirement;
        this.message = message;
        this.starter = starter;
        this.users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
        System.out.println(this.users);
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public long getCredits() {
        return credits;
    }

    public OffsetDateTime getLength() {
        return length;
    }

    public Role getRequirement() {
        return requirement;
    }

    public String getMessage() {
        return message;
    }

    public User getStarter() {
        return starter;
    }
}
