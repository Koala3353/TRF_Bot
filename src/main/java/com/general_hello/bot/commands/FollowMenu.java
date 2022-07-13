package com.general_hello.bot.commands;

import com.general_hello.Config;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.objects.GlobalVariables;
import com.jagrosh.jdautilities.command.MessageContextMenu;
import com.jagrosh.jdautilities.command.MessageContextMenuEvent;
import net.dv8tion.jda.api.entities.Message;

public class FollowMenu extends MessageContextMenu {
    public FollowMenu() {
        this.name = "Follow Champ";
        this.cooldown = 60;
    }

    @Override
    protected void execute(MessageContextMenuEvent event) {
        if (event.getGuild().getIdLong() != Config.getLong("guild")) {
            event.reply("This command is only available in the main server.").setEphemeral(true).queue();
            return;
        }

        if (!event.getTextChannel().getName().startsWith(GlobalVariables.CHAMPS)) {
            event.reply("This command is only available in the champs channel.").setEphemeral(true).queue();
            return;
        }

        Message message = event.getTarget();
        long authorId = DataUtils.getAuthorOfPost(message.getIdLong());
        DataUtils.newFollower(event.getUser().getIdLong(), authorId);
        event.reply("You are now following " + event.getJDA().getUserById(authorId).getAsMention() + ". You will now receive a DM once the champ posts a new message.").setEphemeral(true).queue();
    }
}
