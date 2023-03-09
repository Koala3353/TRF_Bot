package com.general_hello.bot.commands;

import com.general_hello.bot.objects.SelectRole;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;

public class SendSelectRoleMessage extends SlashCommand {
    public SendSelectRoleMessage() {
        this.name = "sendselectrolemessage";
        this.help = "Sends the select role message";
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        SelectRole.send(event.getJDA());
        event.reply("Sent the select role message").setEphemeral(true).queue();
    }
}
