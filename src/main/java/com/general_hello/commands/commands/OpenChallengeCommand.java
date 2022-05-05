package com.general_hello.commands.commands;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.Objects.Challenge;
import com.general_hello.commands.Objects.Level.Rank;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OpenChallengeCommand extends SlashCommand {
    public OpenChallengeCommand() {
        this.name = "open";
        this.help = "Looks for open challenges";
        List<OptionData> dataList = new ArrayList<>();
        OptionData data = new OptionData(OptionType.STRING, "rank", "Create an open challenge command where only a particular Rank can accept");
        for (Rank rank : Rank.values()) {
            data.addChoice(rank.getName(), rank.name());
        }
        dataList.add(data);
        this.options = dataList;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (event.getOption("rank") == null) {
            ArrayList<Challenge> challenges = Challenge.getChallenges();
            ArrayList<String> challengesText = new ArrayList<>();
            for (Challenge challenge : challenges) {
                if (!challenge.isAccepted()) {
                    if (!challenge.isAccepted()) {
                        challengesText.add(challenge.getChallenger().getUser().getAsMention() + "**'s Challenge** - `" + challenge.challengeType() + "` [Press here to go to the challenge](" + challenge.getMessagelink() + ")");
                    }
                }
            }

            // Sending the message and building the paginator
            ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                    .setEventWaiter(Bot.getBot().getEventWaiter())
                    .setItemsPerPage(8)
                    .setTimeout(5, TimeUnit.MINUTES)
                    .useNumberedItems(false);
            if (challengesText.isEmpty()) {
                event.reply("No open challenges").queue();
                return;
            } else {
                event.reply("Loading challenges...").setEphemeral(true).queue();
            }
            builder.setTitle("**Open Challenges**")
                    .setItems(challengesText)
                    .addAllowedUsers(event.getUser().getIdLong())
                    .setColor(0x0099E1);
            builder.build().paginate(event.getTextChannel(), 1);
            return;
        }

        ArrayList<Challenge> challenges = Challenge.getRankChallenge(Rank.valueOf(event.getOption("rank").getAsString()));
        if (challenges == null) {
            event.reply("No open challenges").queue();
            return;
        }
        ArrayList<Long> alreadyThere = new ArrayList<>();
        ArrayList<String> challengesText = new ArrayList<>();
        for (Challenge challenge : challenges) {
            if (!challenge.isAccepted() && !alreadyThere.contains(challenge.getChallenger().getUserid())) {
                alreadyThere.add(challenge.getChallenger().getUserid());
                challengesText.add(challenge.getChallenger().getUser().getAsMention() + "**'s Challenge** - `" + challenge.challengeType() + "` [Press here to go to the challenge](" + challenge.getMessagelink() + ")");
            }
        }

        // Sending the message and building the paginator
        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                .setEventWaiter(Bot.getBot().getEventWaiter())
                .setItemsPerPage(8)
                .setTimeout(5, TimeUnit.MINUTES)
                .useNumberedItems(false);

        if (challengesText.isEmpty()) {
            event.reply("No open challenges").queue();
            return;
        } else {
            event.reply("Loading challenges...").setEphemeral(true).queue();
        }
        builder.setTitle("**" + event.getOption("rank").getAsString() + " Challenges**")
                .setItems(challengesText)
                .addAllowedUsers(event.getUser().getIdLong())
                .setColor(0x0099E1);
        builder.build().paginate(event.getTextChannel(), 1);
    }
}
