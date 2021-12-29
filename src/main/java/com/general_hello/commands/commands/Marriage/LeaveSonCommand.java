package com.general_hello.commands.commands.Marriage;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class LeaveSonCommand extends Command {
    public LeaveSonCommand() {
        this.name = "dump";
        this.cooldown = 259200;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (MarriageData.getSon(event.getAuthor().getIdLong()) == -1) {
            event.reply("Do you even have a son?");
            return;
        }

        MarriageData.leaveSon(event.getAuthor().getIdLong());
        MarriageData.leaveSon(MarriageData.getWife(event.getAuthor().getIdLong()));
        event.reply("Boom! Another child dumped 😟");
    }
}