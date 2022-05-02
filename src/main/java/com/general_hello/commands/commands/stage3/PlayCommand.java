package com.general_hello.commands.commands.stage3;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

public class PlayCommand extends SlashCommand {
    public PlayCommand() {
        this.name = "play";
        this.help = "Shows the map interface and moving of the player";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.reply("Here").queue();
    }
}
