package com.general_hello.commands.commands;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Collections;

public class ProfileCommand extends SlashCommand {
    public ProfileCommand() {
        this.name = "profile";
        this.help = "Check own Profile or other players Profile";
        this.cooldown = 10;
        this.options = Collections.singletonList(new OptionData(OptionType.USER, "user", "The user you want to check the profile of"));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }

        User user = event.getUser();
        if (event.getOption("user") != null) {
            user = event.getOption("user").getAsUser();
        }
        Player player = DataUtils.getPlayer(user);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setTitle(user.getName() + "'s Profile");
        embedBuilder.setThumbnail(user.getEffectiveAvatarUrl());
        embedBuilder.setFooter("Like their profile by pressing the button below!",
                event.getJDA().getSelfUser().getEffectiveAvatarUrl());

        embedBuilder.setDescription("**Discord Name:** " + user.getName() + "\n" +
                "**EXP:** " + player.getExp());
        event.replyEmbeds(embedBuilder.build())
                .addActionRow(Button.secondary(event.getUser().getId() + ":profile", "Like")
                        .withEmoji(Emoji.fromMarkdown("👍"))).queue();
    }
}
