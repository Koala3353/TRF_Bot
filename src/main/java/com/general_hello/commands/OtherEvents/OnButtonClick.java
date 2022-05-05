package com.general_hello.commands.OtherEvents;

import com.general_hello.commands.Objects.Challenge;
import com.general_hello.commands.Objects.Emojis.BotEmojis;
import com.general_hello.commands.Objects.Level.Level;
import com.general_hello.commands.Objects.Level.Rank;
import com.general_hello.commands.Objects.Player;
import com.general_hello.commands.commands.GoogleSheet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;

import static com.general_hello.Config.get;

public class OnButtonClick extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        // users can spoof this id so be careful what you do with this
        String[] id = event.getComponentId().split(":"); // this is the custom id we specified in our button
        String authorId = id[0];

        if (id.length == 1) {
            return;
        }

        String type = id[1];
        if (new Player(event.getUser().getIdLong(), Level.getEnumNameFromChannel(event.getTextChannel())).getGamesFoughtToday() == 11) {
            event.reply("You can't play anymore today.").queue();
            return;
        }
        if (type.equals("winnerAcceptor")) {
            if (!id[2].equals(event.getUser().getId()) && !authorId.equals(event.getUser().getId())) {
                event.reply("You can't press this button").setEphemeral(true).queue();
                return;
            }
            long challengerId = Long.parseLong(id[0]);
            Challenge challenge = Challenge.getChallenge(challengerId);
            Message message = event.getMessage();
            List<MessageEmbed> embeds = message.getEmbeds();
            List<Button> buttons = message.getButtons();
            List<Button> updatedButtons = new ArrayList<>();
            for (Button button : buttons) {
                updatedButtons.add(button.asDisabled());
            }
            challenge.setWinner(challenge.getAcceptor());
            event.getMessage().editMessageEmbeds(embeds).setActionRow(updatedButtons).queue();
            // Send embed
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Confirmation").setColor(Color.GREEN).setFooter("Press the buttons below to confirm.");
            embedBuilder.setDescription("The winner of the game is " + challenge.getAcceptor().getUser().getAsMention() + ".");
            event.getMessage().editMessage(challenge.getChallenger().getUser().getAsMention()).setEmbeds(embedBuilder.build())
                    .setActionRow(
                            Button.success(challenge.getChallenger().getUserid() + ":confirm:" + challenge.getChallenger().getUserid(), "Confirm")
                                    .withEmoji(Emoji.fromMarkdown(BotEmojis.check)),
                            Button.danger(challenge.getChallenger().getUserid() + ":deny:" + challenge.getChallenger().getUserid(), "Deny")
                                    .withEmoji(Emoji.fromMarkdown(BotEmojis.xmark))).queue();
            event.reply("Waiting for confirmation").setEphemeral(true).queue();

            // copy from here
            TimerTask task = new TimerTask() {
                public void run() {
                    Challenge challenge = Challenge.getChallenge(event.getUser().getIdLong());
                    EmbedBuilder embedBuilder = challenge.buildEmbed().setFooter("Game ended.").setDescription(challenge.getChallenger().getUser().getAsMention() + "**'s Challenge:**\n" +
                            "[**Rank:**](" + get("link") + ") " + Rank.getRankFromPoints(challenge.getChallenger().getPoints()).getName() + "\n" +
                            "[**Points:**](" + get("link") + ") " + challenge.getChallenger().getFormattedPoints() + "\n\n" +
                            "[**Challenge Type:**](" + get("link") + ") " + challenge.challengeType() + "\n" +
                            (challenge.isRankOnly() ? "[**Specific Rank:**](" + get("link") + ") " + challenge.getTargetedRank().getName() : "") + "\n\n" +
                            "[**Winner:**](" + get("link") + ") " + challenge.getWinner().getUser().getAsMention());
                    event.getMessage().editMessageEmbeds(embedBuilder.build())
                            .setActionRow(Button.success(challenge.getChallenger().getUserid() + ":confirm:" + challenge.getChallenger().getUserid(), "Confirm")
                                            .withEmoji(Emoji.fromMarkdown(BotEmojis.check)).asDisabled(),
                                    Button.danger(challenge.getChallenger().getUserid() + ":deny:" + challenge.getChallenger().getUserid(), "Deny")
                                            .withEmoji(Emoji.fromMarkdown(BotEmojis.xmark)).asDisabled()).queue();
                    Player challenger = challenge.getChallenger();
                    Player acceptor = challenge.getAcceptor();
                    challenger.addGamesFoughtToday();
                    acceptor.addGamesFoughtToday();
                    Player winnerObj = challenge.getWinner();
                    Player loserObj;
                    Rank rankChallenger = Rank.getRankFromPoints(winnerObj.getPoints());
                    Rank rankAcceptor;
                    if (challenger.getUserid() == winnerObj.getUserid()) {
                        loserObj = acceptor;
                        rankAcceptor = Rank.getRankFromPoints(loserObj.getPoints());
                    } else {
                        loserObj = challenger;
                        rankAcceptor = Rank.getRankFromPoints(loserObj.getPoints());
                    }
                    winnerObj.setPoints(Rank.getPointsWin(rankChallenger, rankAcceptor));
                    loserObj.setPoints(Rank.getPointsLost(rankAcceptor, rankChallenger));
                    GoogleSheet.writeData(event.getTextChannel(), challenge);
                    challenge.delete();
                }
            };
            Timer timer = new Timer("Timer");

            timer.schedule(task, 3600000);
            return;
        } else if (type.equals("winnerChallenger")) {
            if (!id[2].equals(event.getUser().getId()) && !authorId.equals(event.getUser().getId())) {
                event.reply("You can't press this button").setEphemeral(true).queue();
                return;
            }
            long challengerId = Long.parseLong(id[0]);
            Challenge challenge = Challenge.getChallenge(challengerId);
            Message message = event.getMessage();
            List<MessageEmbed> embeds = message.getEmbeds();
            List<Button> buttons = message.getButtons();
            List<Button> updatedButtons = new ArrayList<>();
            for (Button button : buttons) {
                updatedButtons.add(button.asDisabled());
            }
            challenge.setWinner(challenge.getChallenger());
            event.getMessage().editMessageEmbeds(embeds).setActionRow(updatedButtons).queue();
            // Send embed
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Confirmation").setColor(Color.GREEN).setFooter("Press the buttons below to confirm.");
            embedBuilder.setDescription("The winner of the game is " + challenge.getChallenger().getUser().getAsMention() + ".");
            event.getMessage().editMessage(challenge.getAcceptor().getUser().getAsMention()).setEmbeds(embedBuilder.build())
                    .setActionRow(
                            Button.success(challenge.getAcceptor().getUserid() + ":confirm:" + challenge.getChallenger().getUserid(), "Confirm")
                                    .withEmoji(Emoji.fromMarkdown(BotEmojis.check)),
                            Button.danger(challenge.getAcceptor().getUserid() + ":deny:" + challenge.getChallenger().getUserid(), "Deny")
                                    .withEmoji(Emoji.fromMarkdown(BotEmojis.xmark))).queue();
            event.reply("Waiting for confirmation").setEphemeral(true).queue();
            // copy from here
            TimerTask task = new TimerTask() {
                public void run() {
                    Challenge challenge = Challenge.getChallenge(event.getUser().getIdLong());
                    EmbedBuilder embedBuilder = challenge.buildEmbed().setFooter("Game ended.").setDescription(challenge.getChallenger().getUser().getAsMention() + "**'s Challenge:**\n" +
                            "[**Rank:**](" + get("link") + ") " + Rank.getRankFromPoints(challenge.getChallenger().getPoints()).getName() + "\n" +
                            "[**Points:**](" + get("link") + ") " + challenge.getChallenger().getFormattedPoints() + "\n\n" +
                            "[**Challenge Type:**](" + get("link") + ") " + challenge.challengeType() + "\n" +
                            (challenge.isRankOnly() ? "[**Specific Rank:**](" + get("link") + ") " + challenge.getTargetedRank().getName() : "") + "\n\n" +
                            "[**Winner:**](" + get("link") + ") " + challenge.getWinner().getUser().getAsMention());
                    event.getMessage().editMessageEmbeds(embedBuilder.build())
                            .setActionRow(Button.success(challenge.getChallenger().getUserid() + ":confirm:" + challenge.getChallenger().getUserid(), "Confirm")
                                            .withEmoji(Emoji.fromMarkdown(BotEmojis.check)).asDisabled(),
                                    Button.danger(challenge.getChallenger().getUserid() + ":deny:" + challenge.getChallenger().getUserid(), "Deny")
                                            .withEmoji(Emoji.fromMarkdown(BotEmojis.xmark)).asDisabled()).queue();
                    Player challenger = challenge.getChallenger();
                    Player acceptor = challenge.getAcceptor();
                    challenger.addGamesFoughtToday();
                    acceptor.addGamesFoughtToday();
                    Player winnerObj = challenge.getWinner();
                    Player loserObj;
                    Rank rankChallenger = Rank.getRankFromPoints(winnerObj.getPoints());
                    Rank rankAcceptor;
                    if (challenger.getUserid() == winnerObj.getUserid()) {
                        loserObj = acceptor;
                        rankAcceptor = Rank.getRankFromPoints(loserObj.getPoints());
                    } else {
                        loserObj = challenger;
                        rankAcceptor = Rank.getRankFromPoints(loserObj.getPoints());
                    }
                    winnerObj.setPoints(Rank.getPointsWin(rankChallenger, rankAcceptor));
                    loserObj.setPoints(Rank.getPointsLost(rankAcceptor, rankChallenger));
                    GoogleSheet.writeData(event.getTextChannel(), challenge);
                    challenge.delete();
                }
            };
            Timer timer = new Timer("Timer");

            timer.schedule(task, 3600000);
            return;
        }
        // When storing state like this is it is highly recommended doing some kind of verification that it was generated by you, for instance a signature or local cache
        if (!authorId.equals("0000") && !authorId.equals(event.getUser().getId())) {
            event.reply("You can't press this button").setEphemeral(true).queue();
            return;
        }

        switch (type) {
            case "accept" -> {
                long challengerId = Long.parseLong(id[2]);
                Challenge challenge = Challenge.getChallenge(challengerId);
                long acceptor = Long.parseLong(id[0]);
                if (challenge.isRankOnly() || challenge.isGlobal()) {
                    acceptor = event.getUser().getIdLong();
                }
                if (challengerId == event.getUser().getIdLong()) {
                    event.reply("You can't accept your own challenge").setEphemeral(true).queue();
                    return;
                }
                if (challenge.isRankOnly()) {
                    if (challenge.getTargetedRank() != Rank.getRankFromPoints(new Player(acceptor, Level.getEnumNameFromChannel(event.getTextChannel())).getPoints())){
                        event.reply("Your rank needs to be " + challenge.getTargetedRank().getName() + " to accept the challenge").setEphemeral(true).queue();
                        return;
                    }
                }
                challenge.setAccepted(true).setAcceptor(acceptor, Level.getEnumNameFromChannel(event.getTextChannel())).save();
                event.getMessage().editMessageEmbeds(challenge.buildEmbedApproved().build())
                        .setActionRow(Button.secondary(challengerId + ":winnerChallenger:" + challenge.getAcceptor().getUserid(), challenge.getChallenger().getUser().getName() + " Won"),
                                Button.secondary(challengerId + ":winnerAcceptor:" + challenge.getAcceptor().getUserid(), challenge.getAcceptor().getUser().getName() + " Won")).queue();
                event.reply("Challenge accepted!").setEphemeral(true).queue();
            } case "reject" -> {
                long challengerId = Long.parseLong(id[2]);
                if (challengerId == event.getUser().getIdLong()) {
                    event.reply("You can't reject your own challenge").setEphemeral(true).queue();
                    return;
                }
                Challenge challenge = Challenge.getChallenge(challengerId);
                challenge.delete();
                Message message = event.getMessage();
                List<MessageEmbed> embeds = message.getEmbeds();
                List<Button> buttons = message.getButtons();
                List<Button> updatedButtons = new ArrayList<>();
                for (Button button : buttons) {
                    updatedButtons.add(button.asDisabled());
                }
                event.getMessage().editMessageEmbeds(embeds).setActionRow(updatedButtons).queue();
                event.reply("Challenge rejected").queue();
            } case "deny" -> {
                long challengerId = Long.parseLong(id[2]);
                Challenge challenge = Challenge.getChallenge(challengerId);
                event.getMessage().editMessageEmbeds(challenge.buildEmbedApproved().build())
                        .setActionRow(Button.secondary(challengerId + ":winnerChallenger:" + challenge.getAcceptor().getUserid(), challenge.getChallenger().getUser().getName() + " Won"),
                                Button.secondary(challengerId + ":winnerAcceptor:" + challenge.getAcceptor().getUserid(), challenge.getAcceptor().getUser().getName() + " Won")).queue();
                event.reply("Results rejected").queue();
            } case "cancel" -> {
                long challengerId = Long.parseLong(id[0]);
                Challenge challenge = Challenge.getChallenge(challengerId);
                challenge.delete();
                Message message = event.getMessage();
                List<MessageEmbed> embeds = message.getEmbeds();
                List<Button> buttons = message.getButtons();
                List<Button> updatedButtons = new ArrayList<>();
                for (Button button : buttons) {
                    updatedButtons.add(button.asDisabled());
                }
                event.getMessage().editMessageEmbeds(embeds).setActionRow(updatedButtons).queue();
                event.reply("Challenge canceled").queue();
            } case "confirm" -> {
                Challenge challenge = Challenge.getChallenge(event.getUser().getIdLong());
                EmbedBuilder embedBuilder = challenge.buildEmbed().setFooter("Game ended.").setDescription(challenge.getChallenger().getUser().getAsMention() + "**'s Challenge:**\n" +
                        "[**Rank:**](" + get("link") + ") " + Rank.getRankFromPoints(challenge.getChallenger().getPoints()).getName() + "\n" +
                        "[**Points:**](" + get("link") + ") " + challenge.getChallenger().getFormattedPoints() + "\n\n" +
                        "[**Challenge Type:**](" + get("link") + ") " + challenge.challengeType() + "\n" +
                        (challenge.isRankOnly() ? "[**Specific Rank:**](" + get("link") + ") " + challenge.getTargetedRank().getName() : "") + "\n\n" +
                        "[**Winner:**](" + get("link") + ") " + challenge.getWinner().getUser().getAsMention());
                event.getMessage().editMessageEmbeds(embedBuilder.build())
                        .setActionRow(Button.success(challenge.getChallenger().getUserid() + ":confirm:" + challenge.getChallenger().getUserid(), "Confirm")
                                        .withEmoji(Emoji.fromMarkdown(BotEmojis.check)).asDisabled(),
                                Button.danger(challenge.getChallenger().getUserid() + ":deny:" + challenge.getChallenger().getUserid(), "Deny")
                                        .withEmoji(Emoji.fromMarkdown(BotEmojis.xmark)).asDisabled()).queue();
                Player challenger = challenge.getChallenger();
                Player acceptor = challenge.getAcceptor();
                challenger.addGamesFoughtToday();
                acceptor.addGamesFoughtToday();
                Player winnerObj = challenge.getWinner();
                Player loserObj;
                Rank rankChallenger = Rank.getRankFromPoints(winnerObj.getPoints());
                Rank rankAcceptor;
                if (challenger.getUserid() == winnerObj.getUserid()) {
                    loserObj = acceptor;
                    rankAcceptor = Rank.getRankFromPoints(loserObj.getPoints());
                } else {
                    loserObj = challenger;
                    rankAcceptor = Rank.getRankFromPoints(loserObj.getPoints());
                }
                event.reply("Results released").setEphemeral(true).queue();
                winnerObj.setPoints(Rank.getPointsWin(rankChallenger, rankAcceptor));
                loserObj.setPoints(Rank.getPointsLost(rankAcceptor, rankChallenger));
                GoogleSheet.writeData(event.getTextChannel(), challenge);
                challenge.delete();
            }
        }
    }
}