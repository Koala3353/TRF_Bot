package com.general_hello.commands.RPG.RpgUser;

import net.dv8tion.jda.api.entities.User;

public class RPGDataUtils {
    public static String filter(String filterWord) {
        return filterWord.toLowerCase().replace("'", "").replaceAll("\\s+", "");
    }

    /**
     * Returns true if the user started his journey
     *
     * Works by:
     * Making a request to the database. If it's not there it returns -1
     **/
    public static boolean isRPGUser(User user) {
        return RPGUser.getShekels(user.getIdLong()) != -1;
    }

}
