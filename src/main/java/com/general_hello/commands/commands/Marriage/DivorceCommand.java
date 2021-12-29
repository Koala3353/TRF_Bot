package com.general_hello.commands.commands.Marriage;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class DivorceCommand extends Command {
    public DivorceCommand() {
        this.name = "divorce";
        this.cooldown = 259200;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (MarriageData.getWife(event.getAuthor().getIdLong()) == -1) {
            event.reply("Are you even married?");
            return;
        }

        MarriageData.divorce(event.getAuthor().getIdLong());
        MarriageData.divorce(MarriageData.getWife(event.getAuthor().getIdLong()));
        event.reply("Boom! Another couple seperated 😟");
    }
}