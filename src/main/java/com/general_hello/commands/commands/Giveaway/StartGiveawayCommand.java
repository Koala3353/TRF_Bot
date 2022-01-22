package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.OtherEvents.OnSetupMessage;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.*;

public class StartGiveawayCommand extends SlashCommand {
    public StartGiveawayCommand() {
        this.name = "startgiveaway";
        this.help = "Start a credit giveaway! (You use your money)";
        this.ownerCommand = true;
        OptionData[] optionDatas = new OptionData[6];
        OptionData optionData = new OptionData(OptionType.CHANNEL, "channel", "What is the channel where the giveaway will be in?", true);
        optionDatas[0] = optionData;
        optionData = new OptionData(OptionType.INTEGER, "credits", "Amount of credits for the giveaway", true);
        optionDatas[1] = optionData;
        optionData = new OptionData(OptionType.STRING, "length", "Length of the giveaway (4d 5h 1m)", true);
        optionDatas[2] = optionData;
        optionData = new OptionData(OptionType.ROLE, "requirement", "Requirement of the giveaway (role)", true);
        optionDatas[3] = optionData;
        optionData = new OptionData(OptionType.STRING, "message", "Your message to the world :>", true);
        optionDatas[4] = optionData;
        optionData = new OptionData(OptionType.USER, "starter", "The one who hosted of the giveaway", true);
        optionDatas[5] = optionData;
        this.options = Arrays.asList(optionDatas);
    }

    @Override
    public void execute(SlashCommandEvent event) {
        MessageChannel channel = event.getOption("channel").getAsMessageChannel();
        long credits = event.getOption("shekels").getAsLong();
        String length = event.getOption("length").getAsString();
        Role requirement = event.getOption("requirement").getAsRole();
        String message = event.getOption("message").getAsString();
        User starter = event.getOption("starter").getAsUser();
        OffsetDateTime time = OnSetupMessage.stringToTime(length);
        event.reply("Starting the giveaway at " + channel.getName() + ".").queue();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(credits + " credits").setFooter("Giveaway made at", starter.getAvatarUrl());
        embedBuilder.setTimestamp(OffsetDateTime.now());
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setDescription("React with <:heal:912615109430493194>\n to enter!\n" +
                "Time remaining: " + TimeFormat.RELATIVE.format(time) + "\n\n" +
                "Requirement: " + requirement.getAsMention() + "\n" +
                "Message: " + message);

        channel.sendMessage("ㅤㅤㅤㅤ🎉 **Giveaway** 🎉").queue();
        channel.sendMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.primary("0000:join", "Join").withEmoji(Emoji.fromEmote("heal", 912615109430493194L, false)),
                Button.secondary("1234:IGNORE", "Hosted by " + starter.getName()).asDisabled()
        ).queue(message1 -> {
            Giveaway giveaway = new Giveaway(channel, credits, time, requirement, message, starter);
            DataGiveaway.giveawayHashMap.put(message1, giveaway);

            runGiveawayTimer(time, giveaway, message1);
        });
    }

    public static void runGiveawayTimer(OffsetDateTime offsetDateTime, Giveaway giveaway, Message message) {
        Date date = Date.from(offsetDateTime.toInstant());

        Timer timer = new Timer("Timer");
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                User winner = getWinner(giveaway);
                message.editMessageEmbeds(giveawayEndEmbed(giveaway, winner).build()).setActionRow(
                        Button.danger("1234:GAAA", "Giveaway ended!").asDisabled()
                ).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                MessageChannel channel = message.getChannel();
                embedBuilder.setTitle("Giveaway Ended!!!", message.getJumpUrl());
                embedBuilder.setDescription("Amount of Users joined: " + giveaway.getUsers().size() + "\n" +
                        "Odds of Winning: " + ((1/giveaway.getUsers().size()) * 100) + "%");
                embedBuilder.setColor(Color.RED);
                channel.sendMessage(winner.getAsMention()).queue();
                channel.sendMessageEmbeds(embedBuilder.build()).queue();
                DataGiveaway.giveawayHashMap.remove(message);
                timer.cancel();
            }
        };
        timer.schedule(task, date);
    }

    private static EmbedBuilder giveawayEndEmbed(Giveaway giveaway, User winner) {
        long credits = giveaway.getCredits();
        User starter = giveaway.getStarter();
        OffsetDateTime time = giveaway.getLength();
        String message = giveaway.getMessage();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(credits + " credits").setFooter("Giveaway made at", starter.getAvatarUrl());
        embedBuilder.setTimestamp(OffsetDateTime.now());
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setDescription("React with <:heal:912615109430493194> to enter!\n" +
                "Ended at: " + TimeFormat.RELATIVE.format(time) + "\n\n" +
                "Winner: " + (winner == null ? "No one joined!" : winner.getAsMention()) + "\n" +
                "Message: " + message);

        if (winner != null) {
            RPGUser.addShekels(winner.getIdLong(), (int) credits);
        }

        return embedBuilder;
    }

    private static User getWinner(Giveaway giveaway) {
        ArrayList<User> users = giveaway.getUsers();

        if (users.isEmpty()) {
            return null;
        }

        int i = UtilNum.randomNum(0, users.size()-1);
        return users.get(i);
    }
}
