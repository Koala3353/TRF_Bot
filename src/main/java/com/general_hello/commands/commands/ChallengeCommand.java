package com.general_hello.commands.commands;

import com.general_hello.commands.Objects.Challenge;
import com.general_hello.commands.Objects.Emojis.BotEmojis;
import com.general_hello.commands.Objects.Level.Level;
import com.general_hello.commands.Objects.Level.Rank;
import com.general_hello.commands.Objects.Player;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class ChallengeCommand extends SlashCommand {
    public ChallengeCommand() {
        this.name = "challenge";
        this.help = "Create an open challenge.";
        List<OptionData> dataList = new ArrayList<>();
        OptionData data = new OptionData(OptionType.USER, "user", "Create an open challenge command to challenge a particular player");
        dataList.add(data);
        data = new OptionData(OptionType.STRING, "rank", "Create an open challenge command where only a particular Rank can accept");
        for (Rank rank : Rank.values()) {
            data.addChoice(rank.getName(), rank.name());
        }
        dataList.add(data);
        this.options = dataList;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        long challenger = event.getUser().getIdLong();
        if (Challenge.hasChallenge(challenger)) {
            event.reply("You already have an ongoing challenge!").setEphemeral(true).queue();
            return;
        }

        if (new Player(event.getUser().getIdLong(), Level.getEnumNameFromChannel(event.getTextChannel())).getGamesFoughtToday() == 10) {
            event.reply("You can't play anymore today.").queue();
            return;
        }
        if (event.getOption("user") != null) {
            User target = event.getOption("user").getAsUser();
            if (target.isBot()) {
                event.reply("You can't challenge a bot").setEphemeral(true).queue();
                return;
            }
            if (target.getIdLong() == challenger) {
                event.reply("You can't challenge yourself.").setEphemeral(true).queue();
                return;
            }
            Challenge challenge = new Challenge(challenger, new Player(target.getIdLong(), Level.getEnumNameFromChannel(event.getTextChannel())), Level.getEnumNameFromChannel(event.getTextChannel())).save();
            EmbedBuilder embedBuilder = challenge.buildEmbed();
            event.reply(target.getAsMention()).addEmbeds(embedBuilder.build()).addActionRow(
                    Button.success(target.getId() + ":accept:" + challenger, "Accept").withEmoji(Emoji.fromMarkdown(BotEmojis.check)),
                    Button.danger(target.getId() + ":reject:" + challenger, "Reject").withEmoji(Emoji.fromMarkdown(BotEmojis.xmark)),
                    Button.danger(event.getUser().getId() + ":cancel:" + challenger, "Cancel")
                            .withEmoji(Emoji.fromMarkdown(BotEmojis.xmark))).queue();
        } else if (event.getOption("rank") != null) {
            String rankString = event.getOption("rank").getAsString();
            Rank rank = Rank.valueOf(rankString);
            Challenge challenge = new Challenge(challenger, rank, Level.getEnumNameFromChannel(event.getTextChannel())).save();
            EmbedBuilder embedBuilder = challenge.buildEmbed();
            event.replyEmbeds(embedBuilder.build()).addActionRow(
                    Button.success("0000:accept:" + challenger, "Accept").withEmoji(Emoji.fromMarkdown(BotEmojis.check)),
                    Button.danger(event.getUser().getId() + ":cancel", "Cancel")
                            .withEmoji(Emoji.fromMarkdown(BotEmojis.xmark)))
                    .queue((interactionHook) -> interactionHook.retrieveOriginal()
                    .queue((message) -> challenge.setMessagelink(message.getJumpUrl()).save()));
        } else {
            Challenge challenge = new Challenge(challenger, Level.getEnumNameFromChannel(event.getTextChannel())).save();
            EmbedBuilder embedBuilder = challenge.buildEmbed();
            event.replyEmbeds(embedBuilder.build()).addActionRow(
                    Button.success("0000:accept:" + challenger, "Accept").withEmoji(Emoji.fromMarkdown(BotEmojis.check)),
                    Button.danger(event.getUser().getId() + ":cancel", "Cancel")
                            .withEmoji(Emoji.fromMarkdown(BotEmojis.xmark)))
                    .queue((interactionHook) -> interactionHook.retrieveOriginal()
                            .queue((message) -> challenge.setMessagelink(message.getJumpUrl()).save()));
        }
    }
}
