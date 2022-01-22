package com.general_hello.commands.RPG.Commands.Fight;

import com.general_hello.commands.commands.Emoji.Emojis;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class FightCommand extends Command {
    public FightCommand() {
        this.name = "battle";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getMessage().getMentionedUsers().isEmpty()) {
            event.getChannel().sendMessage(Emojis.ERROR + " Kindly mention the user you want to play with.").queue();
            return;
        }

        if (event.getMessage().getMentionedUsers().get(0).isBot()) {
            event.getChannel().sendMessage(Emojis.ERROR + " Kindly mention a user not a " + Emojis.DISCORD_BOT + ".").queue();
            return;
        }

        User toFight = event.getMessage().getMentionedUsers().get(0);
        User challenger = event.getAuthor();
        //Main code
        //Build the embed
        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Challenge to Battle").setFooter("Click on accept if you accept the challenge!");
        embedBuilder.setDescription(challenger.getAsMention() + " is challenging you to a battle!")
                .setTimestamp(OffsetDateTime.now()).setColor(Color.YELLOW);
        event.getTextChannel().sendMessage(toFight.getAsMention()).queue(message -> {
            message.delete().queueAfter(5, TimeUnit.SECONDS);
        });

        String messageID = event.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.primary(toFight.getId() + ":acceptFight:" + challenger.getId(), "Accept")).complete().getId();
        String messageLink = "https://discord.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + messageID;
        //DM the requested user for the channel link
        toFight.openPrivateChannel().queue((privateChannel -> {
            embedBuilder.clear();
            embedBuilder.setTitle("Challenge to Battle");
            embedBuilder.setColor(Color.YELLOW);
            embedBuilder.setDescription(challenger.getName() + " is challenging you to a battle in " + event.getGuild().getName() + " at " + event.getTextChannel().getAsMention() + " in this [message](" + messageLink + ")!");
            privateChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        }));
    }
}
