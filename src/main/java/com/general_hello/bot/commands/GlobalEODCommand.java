package com.general_hello.bot.commands;

import com.general_hello.bot.objects.EODTask;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;

public class GlobalEODCommand extends SlashCommand {
    public GlobalEODCommand() {
        this.name = "globaleod";
        this.help = "Sends an EOD report to all users in the server";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        // run EODTask
        EODTask eodTask = new EODTask(slashCommandEvent.getGuild().getIdLong());
        slashCommandEvent.reply("EOD reports sending!").queue();
        new Thread(eodTask).start();
    }
}
