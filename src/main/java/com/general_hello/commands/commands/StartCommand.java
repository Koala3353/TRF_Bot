package com.general_hello.commands.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;

public class StartCommand extends SlashCommand {
    public StartCommand() {
        this.name = "start";
        this.help = "For players to start/register.";
        this.cooldown = 60;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        User user = event.getUser();
    }
}
