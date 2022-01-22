package com.general_hello.commands.RPG.RpgUser;

import com.general_hello.commands.Bot;
import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.LandAnimal;
import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.Objects.SeaAnimal;
import com.general_hello.commands.RPG.Types.Rarity;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.Register.Data;
import com.general_hello.commands.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RPGDataUtils {
    public static final DecimalFormat formatter = new DecimalFormat("#,###");
    public static int fail = 400;
    public static int MYTHICALODDS = 1;
    public static int LEGGENDARYODDS = 3;
    public static int RAREODDS = 68;
    public static int UNCOMMONODDS = 130;
    public static int COMMONODDS = 1000 - (MYTHICALODDS+LEGGENDARYODDS+RAREODDS+UNCOMMONODDS+fail);

    public static String filter(String filterWord) {
        return filterWord.toLowerCase().replace("'", "").replaceAll("\\s+", "");
    }

    public static boolean checkUser(CommandEvent event) {
        if (!RPGDataUtils.isRPGUser(event.getAuthor())) {
            event.replyError("Kindly start your journey first by doing `ignt journey`");
            return true;
        }

        return false;
    }

    public static boolean checkUser(CommandContext event) {
        if (!RPGDataUtils.isRPGUser(event.getAuthor())) {
            event.getChannel().sendMessage("Kindly start your journey first by doing `ignt journey`").queue();
            return true;
        }

        return false;
    }


    public static ArrayList<SeaAnimal> getSeaArraylistFromRarity(Rarity rarity) {
        switch (rarity) {
            case UNCOMMON:
                return Initializer.uncommonSea;
            case RARE:
                return Initializer.rareSea;
            case LEGENDARY:
                return Initializer.legendarySea;
            case MYTHICAL:
                return Initializer.mythicalSea;
            default:
                return Initializer.commonSea;
        }
    }

    public static ArrayList<LandAnimal> getLandArraylistFromRarity(Rarity rarity) {
        switch (rarity) {
            case UNCOMMON:
                return Initializer.uncommonLand;
            case RARE:
                return Initializer.rareLand;
            case LEGENDARY:
                return Initializer.legendaryLand;
            case MYTHICAL:
                return Initializer.mythicalLand;
            default:
                return Initializer.commonLand;
        }
    }

    public static Rarity getRandomRarity() {
        int randomNum = UtilNum.randomNum(0, 1000-fail);
        randomNum += fail;

        if (randomNum < fail + MYTHICALODDS) {
            return Rarity.MYTHICAL;
        } else if (randomNum < fail + LEGGENDARYODDS + MYTHICALODDS) {
            return Rarity.LEGENDARY;
        } else if (randomNum < fail + RAREODDS + LEGGENDARYODDS + MYTHICALODDS) {
            return Rarity.RARE;
        } else if (randomNum < fail + UNCOMMONODDS + LEGGENDARYODDS + MYTHICALODDS + RAREODDS) {
            return Rarity.UNCOMMON;
        } else {
            return Rarity.COMMON;
        }
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

    public static String getBarFromPercentage(int percentage) {
        String bar = "";

        if (percentage < 10) {
            bar = RPGEmojis.bar1Empty + RPGEmojis.bar2Empty + RPGEmojis.bar2Empty + RPGEmojis.bar3Empty;
        } else if (percentage < 20) {
            bar = RPGEmojis.bar1Half + RPGEmojis.bar2Empty + RPGEmojis.bar2Empty + RPGEmojis.bar3Empty;
        } else if (percentage < 30) {
            bar = RPGEmojis.bar1Full + RPGEmojis.bar2Empty + RPGEmojis.bar2Empty + RPGEmojis.bar3Empty;
        } else if (percentage < 40) {
            bar = RPGEmojis.bar1Full + RPGEmojis.bar2Half + RPGEmojis.bar2Empty + RPGEmojis.bar3Empty;
        } else if (percentage < 50) {
            bar = RPGEmojis.bar1Full + RPGEmojis.bar2High + RPGEmojis.bar2Empty + RPGEmojis.bar3Empty;
        } else if (percentage < 65) {
            bar = RPGEmojis.bar1Full + RPGEmojis.bar2Full + RPGEmojis.bar2Half + RPGEmojis.bar3Empty;
        } else if (percentage < 70) {
            bar = RPGEmojis.bar1Full + RPGEmojis.bar2Full + RPGEmojis.bar2High + RPGEmojis.bar3Empty;
        } else if (percentage < 85) {
            bar = RPGEmojis.bar1Full + RPGEmojis.bar2Full + RPGEmojis.bar2Full + RPGEmojis.bar3Half;
        } else if (percentage < 101) {
            bar = RPGEmojis.bar1Full + RPGEmojis.bar2Full + RPGEmojis.bar2Full + RPGEmojis.bar3Full;
        }

        return bar;
    }

    public static String getBarFromPercentage(int percentage, int outOf) {
        double solving = (double) percentage/outOf;
        solving = solving * 100;

        return getBarFromPercentage((int) solving);
    }

    public static String getNameFromUser(User user) {
        try {
            return Data.userUserPhoneUserHashMap.get(user).getRealName();
        } catch (Exception ignored) {}
        return "Unknown";
    }

    public static String getNameFromUser(long userId) {
        try {
            return Data.userUserPhoneUserHashMap.get(Bot.jda.getUserById(userId)).getRealName();
        } catch (Exception ignored) {}
        return "Unknown";
    }
}