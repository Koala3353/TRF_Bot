package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Objects.SeaAnimal;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.ArrayList;

public class FishCommand extends Command {
    public FishCommand() {
        this.name = "fish";
        this.cooldown = 30;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) {
            return;
        }

        int willGetFish = UtilNum.randomNum(0, 100);
        if (willGetFish < 40) {
            event.reply("Awh man, no fish wanted you rod today. <:fishcommand:923381996804243487>");
            return;
        }

        ArrayList<SeaAnimal> choices;
        choices = RPGDataUtils.getSeaArraylistFromRarity(RPGDataUtils.getRandomRarity());
        int randomNum = UtilNum.randomNum(0, choices.size());
        int amount = UtilNum.randomNum(1, 2);
        SeaAnimal seaAnimal = choices.get(randomNum);
        event.reply("You cast out your line <:fishcommand:923381996804243487> and brought back " + amount + " " + seaAnimal.getName() + " " + seaAnimal.getEmojiOfItem() + "!");
        RPGUser.addItem(event.getAuthor().getIdLong(), amount, RPGDataUtils.filter(seaAnimal.getName()));
    }
}
