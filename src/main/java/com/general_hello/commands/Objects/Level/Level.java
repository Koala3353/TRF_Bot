package com.general_hello.commands.Objects.Level;

import com.general_hello.Bot;
import com.general_hello.Config;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public enum Level {
    PS_LV_125_FOR_HONOR("PS Lv 125 for Honor"),
    PS_LV_150_FOR_HONOR("PS Lv 150 for Honor"),
    PS_LV_125_SURVIVAL("PS Lv 125 Survival"),
    PS_LV_150_SURVIVAL("PS Lv 150 Survival"),
    PC_LV_125_FOR_HONOR("PC Lv 125 for Honor"),
    PC_LV_150_FOR_HONOR("PC Lv 150 for Honor"),
    PC_LV_125_SURVIVAL("PC Lv 125 Survival"),
    PC_LV_150_SURVIVAL("PC Lv 150 Survival");

    // Variables of the object
    private final String name;
    private final long roleId;

    // Initializer


    Level(String name) {
        this.name = name;
        this.roleId = Long.parseLong(Config.get(getConfigName()));
    }

    public String getName() {
        return name;
    }

    public String getDatabaseName() {
        return name.replaceAll("\\s+", "");
    }

    public String getConfigName() {
        return name.replaceAll("\\s+", "").toLowerCase();
    }

    public long getRoleId() {
        return roleId;
    }

    public Role getRole() {
        return Bot.jda.getRoleById(this.roleId);
    }

    public static Level getLevelFromChannel(TextChannel channel) {
        return Level.valueOf(getEnumNameFromChannel(channel));
    }

    public static String getRoleNameFromChannel(TextChannel channel) {
        return channel.getGuild().getRolesByName(channel.getParentCategory().getName()
                .replaceAll("5", "") + " " + channel.getName()
                .replaceAll("-", " "), true).get(0).getName();
    }
    public static String getEnumNameFromChannel(TextChannel channel) {
        return channel.getParentCategory().getName()
                .replaceAll("5", "") + "_" + channel.getName()
                .replaceAll("-", "_").toUpperCase();
    }

}
