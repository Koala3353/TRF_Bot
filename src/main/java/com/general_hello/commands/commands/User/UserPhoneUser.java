package com.general_hello.commands.commands.User;

import com.general_hello.commands.Database.DatabaseManager;
import net.dv8tion.jda.api.entities.User;
public class UserPhoneUser implements Comparable <UserPhoneUser>{
    private String userPhoneUserName;
    private Integer balance;
    private Integer credits;

    private final User discordUser;

    public UserPhoneUser(String userPhoneUserName, User discordUser, Integer credits) {
        this.userPhoneUserName = userPhoneUserName;
        this.discordUser = discordUser;
        this.credits = credits;
    }

    public String getUserPhoneUserName() {
        return userPhoneUserName;
    }

    public void setUserPhoneUserName(String userPhoneUserName) {
        this.userPhoneUserName = userPhoneUserName;
    }

    public Integer getBalance() {
        return balance;
    }

    public Integer getCredits() {
        int credits = DatabaseManager.INSTANCE.getCredits(discordUser.getIdLong());
        this.credits = credits;
        return credits;
    }

    public void setBalance(Integer balance) {
        System.out.println("Balance updated to: " + balance);
        this.balance = balance;
    }

    public User getDiscordUser() {
        return discordUser;
    }

    public int compareTo(UserPhoneUser upu){
        return upu.credits - this.credits;
    }
}