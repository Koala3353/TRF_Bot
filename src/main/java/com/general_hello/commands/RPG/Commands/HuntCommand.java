package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Objects.LandAnimal;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.RPG.Types.Rarity;
import com.general_hello.commands.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;

public class HuntCommand extends Command {
    public  HuntCommand() {
        this.name = "hunt";
        this.cooldown = 20;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) {
            return;
        }

        if (event.getArgs().equalsIgnoreCase("table")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Loot table for hunting");
            double legendary = RPGDataUtils.LEGGENDARYODDS / (double) 10;
            double mythical = RPGDataUtils.MYTHICALODDS / (double) 10;
            double rare = RPGDataUtils.RAREODDS / (double) 10;
            embedBuilder.setDescription("**Fail** - `" + RPGDataUtils.fail/10 + "%`\n\n" +
                    "**Common** - `" + RPGDataUtils.COMMONODDS/10 + "%`\n" +
                    "Items: " + displayLandItemsFromRarity(Rarity.COMMON) +
                    "\n\n" +
                    "**Uncommon** - `" + RPGDataUtils.UNCOMMONODDS/10 + "%`\n" +
                    "Items: " + displayLandItemsFromRarity(Rarity.UNCOMMON) +
                    "\n\n" +
                    "**Rare** - `" + rare + "%`\n" +
                    "Items: " + displayLandItemsFromRarity(Rarity.RARE) +
                    "\n\n" +
                    "**Legendary** - `" + legendary + "%`\n" +
                    "Items: " + displayLandItemsFromRarity(Rarity.LEGENDARY) +
                    "\n\n" +
                    "**Mythical** - `" + mythical + "%`\n" +
                    "Items: " + displayLandItemsFromRarity(Rarity.MYTHICAL));
            event.reply(embedBuilder.build());
            return;
        }

        if (RPGUser.getItemCount(event.getAuthor().getIdLong(), "huntingrifle") < 1) {
            event.replyError("You need a hunting riffle to hunt!!! Buy one now by doing `ignt buy huntingrifle`");
            return;
        }

        if (UtilNum.randomNum(1, 100) > 95) {
            event.reply("Your hunting riffle exploded while your trying to kill a gnat! Luckily, you survived.");
            RPGUser.addItem(event.getAuthor().getIdLong(), -1, "huntingrifle");
            return;
        }

        int willGetFish = UtilNum.randomNum(0, 1000);
        if (willGetFish < RPGDataUtils.fail) {
            event.reply("All that time in the woods, and you couldn't catch a single thing hahaha 🤪");
            return;
        }

        ArrayList<LandAnimal> choices;
        choices = RPGDataUtils.getLandArraylistFromRarity(RPGDataUtils.getRandomRarity());
        int randomNum = UtilNum.randomNum(0, choices.size()-1);
        LandAnimal landAnimal = choices.get(randomNum);
        event.reply("You went hunting and brought back a " + landAnimal.getName() + " " + landAnimal.getEmojiOfItem() + "!");
        RPGUser.addItem(event.getAuthor().getIdLong(), 1, RPGDataUtils.filter(landAnimal.getName()));
    }

    private static String displayLandItemsFromRarity(Rarity rarity) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<LandAnimal> landArraylistFromRarity = RPGDataUtils.getLandArraylistFromRarity(rarity);
        int x = 0;

        while (x < landArraylistFromRarity.size()) {
            LandAnimal landAnimal = landArraylistFromRarity.get(x);
            stringBuilder.append(landAnimal.getEmojiOfItem()).append(" ").append(landAnimal.getName());
            if (x != landArraylistFromRarity.size()-1) {
                stringBuilder.append(", ");
            }
            x++;
        }

        return stringBuilder.toString();
    }
}
