package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Objects.LandAnimal;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.ArrayList;

public class HuntCommand extends Command {
    public HuntCommand() {
        this.name = "hunt";
        this.cooldown = 30;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) {
            return;
        }

        int willGetFish = UtilNum.randomNum(0, 100);
        if (willGetFish < 40) {
            event.reply("All that time in the woods, and you couldn't catch a single thing hahaha 🤪");
            return;
        }

        ArrayList<LandAnimal> choices;
        choices = RPGDataUtils.getLandArraylistFromRarity(RPGDataUtils.getRandomRarity());
        int randomNum = UtilNum.randomNum(0, choices.size());
        LandAnimal landAnimal = choices.get(randomNum);
        event.reply("You went hunting and brought back a " + landAnimal.getName() + " " + landAnimal.getEmojiOfItem() + "!");
        RPGUser.addItem(event.getAuthor().getIdLong(), 1, RPGDataUtils.filter(landAnimal.getName()));
    }
}
