package com.general_hello.bot.commands;

import com.general_hello.Config;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.objects.GlobalVariables;
import com.jagrosh.jdautilities.command.MessageContextMenu;
import com.jagrosh.jdautilities.command.MessageContextMenuEvent;
import net.dv8tion.jda.api.entities.Message;

public class UnfollowMenu extends MessageContextMenu {
    public UnfollowMenu() {
        this.name = "Unfollow Champ";
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

        if (!DataUtils.hasAccount(event.getUser().getIdLong())) {
            event.reply("Kindly make an account first by using `/register`").setEphemeral(true).queue();
            return;
        }

        if (DataUtils.isBanned(event.getUser().getIdLong())) {
            event.reply("You are banned from using the bot.").setEphemeral(true).queue();
            return;
        }

        Message message = event.getTarget();
        long authorId = DataUtils.getAuthorOfPost(message.getIdLong());
        DataUtils.removeFollower(event.getUser().getIdLong(), authorId);
        event.reply("You successfully unfollowed " + event.getJDA().getUserById(authorId).getAsMention() + ". You will not receive a DM anymore once the champ posts a new message.").setEphemeral(true).queue();
    }
}
