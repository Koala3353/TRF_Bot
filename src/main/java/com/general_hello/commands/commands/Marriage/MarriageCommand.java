package com.general_hello.commands.commands.Marriage;

import com.general_hello.commands.Bot;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Info.InfoUserCommand;
import com.general_hello.commands.commands.RankingSystem.LevelPointManager;
import com.general_hello.commands.commands.Register.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.IOException;
import java.sql.SQLException;

public class MarriageCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        User author = ctx.getAuthor();
        if (MarriageData.getWife(author.getIdLong()) == -1) {
            if (ctx.getMessage().getMentionedMembers().isEmpty()) {
                ctx.getChannel().sendMessage("Kindly mention your dear wife (or husband ofc)!").queue();
                return;
            }

            Member member = ctx.getMessage().getMentionedMembers().get(0);
            if (member.equals(ctx.getMember()) || member.getUser().isBot()) {
                ctx.getChannel().sendMessage("Ye shall not marry himself (or herself) or any good bots.").queue();
                return;
            }

            if (MarriageData.getWife(author.getIdLong()) != -1) {
                ctx.getChannel().sendMessage("The one you wish to marry has chosen another husband/wife instead of you. 😭").queue();
                return;
            }
            ctx.getChannel().sendMessage(member.getAsMention()).queue();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Pending Confirmation").setDescription(author.getAsMention() + " is proposing to you!\n" +
                    "**What is your answer?**");
            ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(
                    Button.danger(member.getId() + ":noMarry:" + author.getId(), "No"),
                    Button.success(member.getId() + ":yesMarry:" + author.getId(), "Yes")
            ).queue();
            return;
        }

        makeEmbed(ctx.getChannel(), author, "Nothing");
    }

    @Override
    public String getName() {
        return "marry";
    }

    @Override
    public String getHelp(String prefix) {
        return "Love and marry!!!";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }

    private static String emptyAt(int percent) {
        double number = (double) percent/MarriageData.decreaseBy;
        int hours = (int) number;
        return hours + " hours";
    }

    public static void makeEmbed(TextChannel textChannel, User author, String latestAction) {
        int happiness = (int) MarriageData.getHappiness(author.getIdLong());
        int love = (int) MarriageData.getLove(author.getIdLong());
        int houseStatus = (int) MarriageData.getHouseStatus(author.getIdLong());
        int experience = (int) MarriageData.getXP(author.getIdLong());
        int experienceLevel = (int) LevelPointManager.calculateLevel(experience);
        int experienceLevelMax = (int) LevelPointManager.calculateLevelMax(experienceLevel);
        int house = (int) MarriageData.getHouseXP(author.getIdLong());
        int houseLevel = (int) LevelPointManager.calculateLevel(house);
        int houseLevelMax = (int) LevelPointManager.calculateLevelMax(houseLevel);
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(InfoUserCommand.randomColor());
        embedBuilder.setTitle("Marriage Data of " + author.getName());
        embedBuilder.addField("Happiness", RPGDataUtils.getBarFromPercentage(happiness) + " (" + happiness + "%)\n" +
                "`" + emptyAt(happiness) + " until empty`", true);
        embedBuilder.addField("Love", RPGDataUtils.getBarFromPercentage(love) + " (" + love + "%)\n" +
                "`" + emptyAt(love) + " until empty`", true);
        embedBuilder.addField("House Status", RPGDataUtils.getBarFromPercentage(happiness) + " (" + houseStatus + "%)\n" +
                "`" + emptyAt(houseStatus) + " until empty`", true);
        embedBuilder.addField("Experience", RPGDataUtils.getBarFromPercentage(experience, experienceLevelMax) + " (" + RPGDataUtils.formatter.format(experience) + "/" + RPGDataUtils.formatter.format(experienceLevelMax) + ")\n" +
                "`Level " + experienceLevel + "`", true);
        embedBuilder.addField("House Level", RPGDataUtils.getBarFromPercentage(house, houseLevelMax) + " (" + RPGDataUtils.formatter.format(house) + "/" + RPGDataUtils.formatter.format(houseLevelMax) + ")\n" +
                "`Level " + houseLevel + "`", true);
        String husbandName = "Unknown";
        String wifeName = "Unknown";
        String[] split;
        try {
            husbandName = Data.userUserPhoneUserHashMap.get(author).getUserPhoneUserName();
            split = husbandName.split("\\s+");
            husbandName = split[0];
        } catch (Exception ignored) {}

        try {
            wifeName = Data.userUserPhoneUserHashMap.get(Bot.jda.getUserById(MarriageData.getWife(author.getIdLong()))).getUserPhoneUserName();
            split = wifeName.split("\\s+");
            wifeName = split[0];
        } catch (Exception ignored) {}
        embedBuilder.addField("Last Action", "`" + latestAction + "`", false);
        embedBuilder.setFooter("Tip: You can't increase a stat if it is already above 90%!");
        boolean disableHappy = false;
        boolean disableLove = false;
        boolean disableHouse = false;
        if (happiness > 90) {
            disableHappy = true;
        }

        if (love > 90) {
            disableLove = true;
        }

        if (houseStatus > 90) {
            disableHouse = true;
        }

        textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.primary(author.getId() + ":date", "Date").withDisabled(disableHappy),
                Button.primary(author.getId() + ":hug", "Hug").withDisabled(disableLove),
                Button.primary(author.getId() + ":fixHouse", "Fix House").withDisabled(disableHouse),
                Button.secondary("IGNOREMYSADFACE", husbandName + " ✗ " + wifeName).asDisabled()
        ).queue();
    }

    public static void makeEmbed(Message message, User author, String latestAction) {
        int happiness = (int) MarriageData.getHappiness(author.getIdLong());
        int love = (int) MarriageData.getLove(author.getIdLong());
        int houseStatus = (int) MarriageData.getHouseStatus(author.getIdLong());
        int experience = (int) MarriageData.getXP(author.getIdLong());
        int experienceLevel = (int) LevelPointManager.calculateLevel(experience);
        int experienceLevelMax = (int) LevelPointManager.calculateLevelMax(experienceLevel);
        int house = (int) MarriageData.getHouseXP(author.getIdLong());
        int houseLevel = (int) LevelPointManager.calculateLevel(house);
        int houseLevelMax = (int) LevelPointManager.calculateLevelMax(houseLevel);
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(InfoUserCommand.randomColor());
        embedBuilder.setTitle("Marriage Data of " + author.getName());
        embedBuilder.addField("Happiness", RPGDataUtils.getBarFromPercentage(happiness) + " (" + happiness + "%)\n" +
                "`" + emptyAt(happiness) + " until empty`", true);
        embedBuilder.addField("Love", RPGDataUtils.getBarFromPercentage(love) + " (" + love + "%)\n" +
                "`" + emptyAt(love) + " until empty`", true);
        embedBuilder.addField("House Status", RPGDataUtils.getBarFromPercentage(houseStatus) + " (" + houseStatus + "%)\n" +
                "`" + emptyAt(houseStatus) + " until empty`", true);
        embedBuilder.addField("Experience", RPGDataUtils.getBarFromPercentage(experience, experienceLevelMax) + " (" + RPGDataUtils.formatter.format(experience) + "/" + RPGDataUtils.formatter.format(experienceLevelMax) + ")\n" +
                "`Level " + experienceLevel + "`", true);
        embedBuilder.addField("House Level", RPGDataUtils.getBarFromPercentage(house, houseLevelMax) + " (" + RPGDataUtils.formatter.format(house) + "/" + RPGDataUtils.formatter.format(houseLevelMax) + ")\n" +
                "`Level " + houseLevel + "`", true);
        String husbandName = "Unknown";
        String wifeName = "Unknown";
        String[] split;
        try {
            husbandName = Data.userUserPhoneUserHashMap.get(author).getUserPhoneUserName();
            split = husbandName.split("\\s+");
            husbandName = split[0];
        } catch (Exception ignored) {}

        try {
            wifeName = Data.userUserPhoneUserHashMap.get(Bot.jda.getUserById(MarriageData.getWife(author.getIdLong()))).getUserPhoneUserName();
            split = wifeName.split("\\s+");
            wifeName = split[0];
        } catch (Exception ignored) {}
        embedBuilder.addField("Last Action", "`" + latestAction + "`", false);
        embedBuilder.setFooter("Tip: You can't increase a stat if it is already above 90%!");
        boolean disableHappy = false;
        boolean disableLove = false;
        boolean disableHouse = false;
        if (happiness > 90) {
            disableHappy = true;
        }

        if (love > 90) {
            disableLove = true;
        }

        if (houseStatus > 90) {
            disableHouse = true;
        }

        message.editMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.primary(author.getId() + ":date", "Date").withDisabled(disableHappy),
                Button.primary(author.getId() + ":hug", "Hug").withDisabled(disableLove),
                Button.primary(author.getId() + ":fixHouse", "Fix House").withDisabled(disableHouse),
                Button.secondary("IGNOREMYSADFACE", husbandName + " ✗ " + wifeName).asDisabled()
        ).queue();
    }
}
