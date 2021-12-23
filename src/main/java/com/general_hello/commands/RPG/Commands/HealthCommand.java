package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class HealthCommand extends Command {
    public HealthCommand() {
        this.name = "health";
        this.cooldown = 30;
    }

    @Override
    protected void execute(CommandEvent event) {
        User user = event.getAuthor();
        if (!event.getMessage().getMentionedUsers().isEmpty()) {
            user = event.getMessage().getMentionedUsers().get(0);
        }

        if (RPGDataUtils.checkUser(event)) return;

        int health = RPGUser.getHealth(user.getIdLong());
        event.reply("The health of " + user.getName() + " is <:heal:912615109430493194> **" + health +  "**");
    }
}
