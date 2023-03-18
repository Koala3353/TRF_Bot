package com.general_hello.bot.utils;

import com.general_hello.Config;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.objects.CustomQuestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EODUtil {
    public static ArrayList<Long> secondTimeUsers = new ArrayList<>();

    public static void newMemberEODReport(Member newMember) {
        // Send New Member EOD report
        EmbedBuilder eodReportEmbed = getEODReportEmbed(newMember, 0, OffsetDateTime.now(ZoneId.of("UTC-6")).toString());
        newMember.getUser().openPrivateChannel()
                .flatMap((channel) ->
                        channel.sendMessageEmbeds(eodReportEmbed.build()).setComponents(getActionRow(0, OffsetDateTime.now(ZoneId.of("UTC-6")).toString())))
                .queue(
                        message -> Util.logInfo("Successfully sent EOD report to new member: " + newMember.getUser().getAsTag(), EODUtil.class),
                        throwable -> {
                            Util.logError("Failed to send EOD report to new member: " + newMember.getUser().getAsTag(), EODUtil.class);
                            newMember.getGuild().getTextChannelById(Config.getLong("error_channel")).sendMessage(newMember.getAsMention() + " kindly enable your DMs!").queue();
                        }
                );
    }

    public static void newDailyEODReport(Member newMember) {
        // Send New Member EOD report
        EmbedBuilder eodReportEmbed = getEODReportEmbed(newMember, 1, OffsetDateTime.now(ZoneId.of("UTC-6")).toString());
        if (!newMember.getUser().getId().equals(newMember.getJDA().getSelfUser().getId())) {
            newMember.getUser().openPrivateChannel()
                    .flatMap((channel) ->
                            channel.sendMessageEmbeds(eodReportEmbed.build()).setComponents(getActionRow(0, OffsetDateTime.now(ZoneId.of("UTC-6")).toString())))
                    .queue(
                            message -> Util.logInfo("Successfully sent daily EOD report to member: " + newMember.getUser().getAsTag(), EODUtil.class),
                            throwable -> {
                                Util.logError("Failed to send daily EOD report to member: " + newMember.getUser().getAsTag(), EODUtil.class);
                                Util.logInfo("Attempting to send EOD report to " + newMember.getGuild().getName() + " instead.", EODUtil.class);
                                Util.createCategoryIfDoesntExist(newMember.getGuild(), "EOD Report");
                                // delay for 3 seconds
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Util.createChannelIfDoesntExist(newMember.getGuild(), "eod-report-" + newMember.getId(), "EOD Report", newMember);
                                // delay for 3 seconds
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Util.sendEodReportToChannel(newMember, 1);
                            }
                    );
        }
    }

    public static EmbedBuilder getEODReportEmbed(Member member, int questionNumber, String date) {
        // Get EOD report embed
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setFooter(member.getGuild().getName() + ": " + date.split("T")[0], member.getGuild().getIconUrl());

        switch (questionNumber) {
            case 0:
                // First timer embed
                embedBuilder.setTitle("First EOD Report");
                embedBuilder.setDescription("Hello " + member.getAsMention() + ", welcome to **" + member.getGuild().getName() + "**! " +
                        "This is a report that will help you to keep track of your streak. " +
                        "Please answer the following questions as honestly as possible.");
                embedBuilder.setColor(Color.BLUE);
                embedBuilder.addField("Question 1", "Did you relapse?", false);
                break;
            case 1:
                embedBuilder.setTitle("Daily EOD Report");
                embedBuilder.setDescription("DO NOT FILL THIS OUT RIGHT AFTER IT IS SENT TO YOU UNLESS IT IS ACTUALLY THE END OF YOUR DAY!");
                embedBuilder.addField("Question 1", "Did you relapse?", false);
                break;
            case 2:
                embedBuilder.setTitle("Daily EOD Report");
                embedBuilder.addField("Question 2", "What color are you?", false);
                break;
            case 3:
                embedBuilder.setTitle("Daily EOD Report");
                embedBuilder.addField("Question 3", "Daily Ranking", false);
                break;
            case 99:
                long idLong = member.getIdLong();
                // End of EOD report embed
                OffsetDateTime oneDayLater = OffsetDateTime.now(ZoneId.of("UTC-6")).withHour(0).withMinute(0).withSecond(0);
                // check if onedaylater is before now
                if (oneDayLater.isBefore(OffsetDateTime.now(ZoneId.of("UTC-6")))) {
                    oneDayLater = oneDayLater.plusDays(1);
                }

                String userAsTag = member.getUser().getAsTag();
                embedBuilder.setAuthor(userAsTag, null, member.getUser().getAvatarUrl());
                // Set Color
                String color = DataUtils.getColor(member.getIdLong());
                Color colorFromPlainText = DataUtils.getColorFromPlainText(color);
                embedBuilder.setColor(colorFromPlainText);
                // Set Relapse
                boolean didRelapse = Boolean.TRUE.equals(DataUtils.getBooleanFromInt(DataUtils.getRelapse(idLong)));
                if (didRelapse) {
                    embedBuilder.addField("Relapse", "Yes", false);
                } else {
                    embedBuilder.addField("Relapse", "No", false);
                }
                // if date is not today's date
                if (!date.split("T")[0].equals(OffsetDateTime.now(ZoneId.of("UTC-6")).toString().split("T")[0])) {
                    embedBuilder.addField("Late EOD Report", date.split("T")[0], false);
                }
                // Set Color Text
                embedBuilder.addField("Color", DataUtils.getColorFancyText(color), false);
                // Set Daily ranking
                embedBuilder.addField("Daily Ranking", String.valueOf(DataUtils.getUrge(idLong)), false);
                // Set Other questions
                List<String> questionsList = DataUtils.getQuestionsList(idLong);
                LinkedList<String> answers = CustomQuestion.userToAnswers.get(idLong);
                if (!questionsList.isEmpty()) {
                    for (int i = 0; i < questionsList.size(); i++) {
                        embedBuilder.addField(questionsList.get(i), answers.get(i), false);
                    }
                }

                embedBuilder.setTitle("Daily EOD Report");
                embedBuilder.setDescription("Thank you for answering the questions. " +
                        "Your next EOD Report will be " + Util.getTimestampDiscord(oneDayLater) + ".");
                embedBuilder.setColor(Color.GREEN);
                break;
            case 100:
                // Summary EOD report
                idLong = member.getIdLong();
                embedBuilder.setTitle("Summary of EOD Report");
                userAsTag = member.getUser().getAsTag();
                embedBuilder.setAuthor(userAsTag, null, member.getUser().getAvatarUrl());
                // Set Color
                color = DataUtils.getColor(idLong);
                colorFromPlainText = DataUtils.getColorFromPlainText(color);
                embedBuilder.setColor(colorFromPlainText);
                // Send message based on color
                if (color != null) {
                    String leaderId = Util.getChannelFromRole(member).getTopic();
                    String[] split = leaderId.split(",");
                    if (color.equals("Yellow")) {
                        Util.sendDM(split[0], userAsTag + " is yellow today.");
                    } else if (color.equals("Red")) {
                        Util.sendDM(split[1], userAsTag + " is red today.");
                    }
                }
                // Set Relapse
                didRelapse = Boolean.TRUE.equals(DataUtils.getBooleanFromInt(DataUtils.getRelapse(idLong)));
                if (didRelapse) {
                    embedBuilder.addField("Relapse", "Yes", false);
                } else {
                    embedBuilder.addField("Relapse", "No", false);
                }
                // if date is not today's date
                if (!date.split("T")[0].equals(OffsetDateTime.now(ZoneId.of("UTC-6")).toString().split("T")[0])) {
                    embedBuilder.addField("Late EOD Report", date.split("T")[0], false);
                }
                // Set Color Text
                embedBuilder.addField("Color", DataUtils.getColorFancyText(color), false);
                // Set Daily ranking
                embedBuilder.addField("Daily Ranking", String.valueOf(DataUtils.getUrge(idLong)), false);
                // Set Other questions
                questionsList = DataUtils.getQuestionsList(idLong);
                answers = CustomQuestion.userToAnswers.get(idLong);
                if (!questionsList.isEmpty()) {
                    for (int i = 0; i < questionsList.size(); i++) {
                        embedBuilder.addField(questionsList.get(i), answers.get(i), false);
                    }
                }
            default:
                if (questionNumber <= 13) {
                    embedBuilder.setTitle("Daily EOD Report");
                    questionsList = DataUtils.getQuestionsList(member.getIdLong());
                    if (!questionsList.isEmpty()) {
                        embedBuilder.addField("Question " + questionNumber, questionsList.get(questionNumber - 4), false);
                    }
                }
                break;
        }
        return embedBuilder;
    }

    public static ActionRow getActionRow(int questionNumber, String date) {
        // action row creator
        switch (questionNumber) {
            case 0, 1:
                return ActionRow.of(
                        Button.danger("0000:eodReport:RelapseYes:" + date, "Yes"),
                        Button.success("0000:eodReport:RelapseNo:" + date, "No")
                );
            case 2:
                return ActionRow.of(
                        Button.success("0000:eodReport:ColorGreen:" + date, "Green"),
                        Button.secondary("0000:eodReport:ColorYellow:" + date, "Yellow"),
                        Button.danger("0000:eodReport:ColorRed:" + date, "Red")
                );
            case 3:
                return ActionRow.of(
                        StringSelectMenu.create("0000:eodReport:UrgeMeter:" + date)
                                .setPlaceholder("Daily Ranking")
                                .addOption("1", "1")
                                .addOption("2", "2")
                                .addOption("3", "3")
                                .addOption("4", "4")
                                .addOption("5", "5")
                                .addOption("6", "6")
                                .addOption("7", "7")
                                .addOption("8", "8")
                                .addOption("9", "9")
                                .addOption("10", "10")
                                .setRequiredRange(1, 1)
                                .build());
            default:
                return ActionRow.of(
                        Button.success("0000:customQuestionAnswer:" + questionNumber + ":" + date + ":Yes", "Yes"),
                        Button.danger("0000:customQuestionAnswer:" + questionNumber + ":" + date + ":No", "No")
                );
        }
    }
}