package com.general_hello.commands.RPG.RpgUser;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.LandAnimal;
import com.general_hello.commands.RPG.Objects.SeaAnimal;
import com.general_hello.commands.RPG.Types.Rarity;
import com.general_hello.commands.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RPGDataUtils {
    public static final DecimalFormat formatter = new DecimalFormat("#,###.00");
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
        int randomNum = UtilNum.randomNum(0, 1000);

        if (randomNum < 2) {
            return Rarity.MYTHICAL;
        } else if (randomNum < 12) {
            return Rarity.LEGENDARY;
        } else if (randomNum < 112) {
            return Rarity.RARE;
        } else if (randomNum < 412) {
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

}