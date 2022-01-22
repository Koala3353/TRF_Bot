package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Objects.SeaAnimal;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.RPG.Types.Rarity;
import com.general_hello.commands.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;

public class FishCommand extends Command {
    public FishCommand() {
        this.name = "fish";
        this.cooldown = 20;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) {
            return;
        }

        if (event.getArgs().equalsIgnoreCase("table")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Loot table for fishing");
            double legendary = RPGDataUtils.LEGGENDARYODDS / (double) 10;
            double mythical = RPGDataUtils.MYTHICALODDS / (double) 10;
            double rare = RPGDataUtils.RAREODDS / (double) 10;
            embedBuilder.setDescription("**Fail** - `" + RPGDataUtils.fail/10 + "%`\n\n" +
                    "**Common** - `" + RPGDataUtils.COMMONODDS/10 + "%`\n" +
                    "Items: " + displayWaterItemsFromRarity(Rarity.COMMON) +
                    "\n\n" +
                    "**Uncommon** - `" + RPGDataUtils.UNCOMMONODDS/10 + "%`\n" +
                    "Items: " + displayWaterItemsFromRarity(Rarity.UNCOMMON) +
                    "\n\n" +
                    "**Rare** - `" + rare + "%`\n" +
                    "Items: " + displayWaterItemsFromRarity(Rarity.RARE) +
                    "\n\n" +
                    "**Legendary** - `" + legendary + "%`\n" +
                    "Items: " + displayWaterItemsFromRarity(Rarity.LEGENDARY) +
                    "\n\n" +
                    "**Mythical** - `" + mythical + "%`\n" +
                    "Items: " + displayWaterItemsFromRarity(Rarity.MYTHICAL));
            event.reply(embedBuilder.build());
            return;
        }

        if (RPGUser.getItemCount(event.getAuthor().getIdLong(), "fishingpole") < 1) {
            event.replyError("You need a fishing pole to fish!!! Buy one now by doing `ignt buy fishingpole`");
            return;
        }

        if (UtilNum.randomNum(1, 100) > 95) {
            event.reply("Your fishing pole broke while trying to catch the **Kraken**!");
            RPGUser.addItem(event.getAuthor().getIdLong(), -1, "fishingpole");
            return;
        }

        int willGetFish = UtilNum.randomNum(0, 1000);
        if (willGetFish < RPGDataUtils.fail) {
            event.reply("Awh man, no fish wanted your rod today. <:fishcommand:923381996804243487>");
            return;
        }

        ArrayList<SeaAnimal> choices;
        choices = RPGDataUtils.getSeaArraylistFromRarity(RPGDataUtils.getRandomRarity());
        int randomNum = UtilNum.randomNum(0, choices.size()-1);
        int amount = UtilNum.randomNum(1, 2);
        SeaAnimal seaAnimal = choices.get(randomNum);
        event.reply("You cast out your line <:fishcommand:923381996804243487> and brought back " + amount + " " + seaAnimal.getName() + " " + seaAnimal.getEmojiOfItem() + "!");
        RPGUser.addItem(event.getAuthor().getIdLong(), amount, RPGDataUtils.filter(seaAnimal.getName()));
    }

    private static String displayWaterItemsFromRarity(Rarity rarity) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<SeaAnimal> seaArraylistFromRarity = RPGDataUtils.getSeaArraylistFromRarity(rarity);
        int x = 0;

        while (x < seaArraylistFromRarity.size()) {
            SeaAnimal seaAnimal = seaArraylistFromRarity.get(x);
            stringBuilder.append(seaAnimal.getEmojiOfItem()).append(" ").append(seaAnimal.getName());
            if (x != seaArraylistFromRarity.size()-1) {
                stringBuilder.append(", ");
            }
            x++;
        }

        return stringBuilder.toString();
    }
}
