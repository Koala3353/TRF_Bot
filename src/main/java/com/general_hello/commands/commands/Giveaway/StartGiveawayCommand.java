package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.OtherEvents.OnSetupMessage;
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
        String[] users = new String[4];
        users[0] = "755975812909367387";
        users[1] = "756308319622397972";
        users[2] = "512126539014340608";
        users[3] = "846730220903333899";
        this.enabledUsers = users;
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
    protected void execute(SlashCommandEvent event) {
        event.reply("Creating the giveaway...").queue();
        MessageChannel channel = event.getOption("channel").getAsMessageChannel();
        long credits = event.getOption("credits").getAsLong();
        String length = event.getOption("length").getAsString();
        Role requirement = event.getOption("requirement").getAsRole();
        String message = event.getOption("message").getAsString();
        User starter = event.getOption("starter").getAsUser();
        OffsetDateTime time = OnSetupMessage.stringToTime(length);

        DatabaseManager.INSTANCE.setCredits(event.getUser().getIdLong(), (int) (-credits));

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
        /*ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(StartGiveawayCommand::run, 0, 1, TimeUnit.DAYS);*/

        Date date = Date.from(offsetDateTime.toInstant());

        Timer timer = new Timer("Timer");
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                message.editMessageEmbeds(giveawayEndEmbed(giveaway).build()).setActionRow(
                        Button.danger("1234:GAAA", "Giveaway ended!").asDisabled()
                ).queue();

                DataGiveaway.giveawayHashMap.remove(message);
                timer.cancel();
            }
        };
        timer.schedule(task, date);
    }

    private static EmbedBuilder giveawayEndEmbed(Giveaway giveaway) {
        long credits = giveaway.getCredits();
        User starter = giveaway.getStarter();
        OffsetDateTime time = giveaway.getLength();
        String message = giveaway.getMessage();
        User winner = getWinner(giveaway);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(credits + " credits").setFooter("Giveaway made at", starter.getAvatarUrl());
        embedBuilder.setTimestamp(OffsetDateTime.now());
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setDescription("React with <:heal:912615109430493194> to enter!\n" +
                "Ended at: " + TimeFormat.RELATIVE.format(time) + "\n\n" +
                "Winner: " + (winner == null ? "No one joined!" : winner.getAsMention()) + "\n" +
                "Message: " + message);

        if (winner != null) {
            DatabaseManager.INSTANCE.setCredits(winner.getIdLong(), (int) credits);
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
