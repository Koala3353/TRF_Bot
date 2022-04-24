package com.general_hello.commands.Database;

import com.general_hello.commands.Objects.BotEmojis;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class DataUtils {
    private static HashMap<User, Player> players = new HashMap<>();
    public static final DecimalFormat formatter = new DecimalFormat("#,###");

    public static String filter(String filterWord) {
        return filterWord.toLowerCase().replace("'", "").replaceAll("\\s+", "");
    }

    public static String getBarFromPercentage(int percentage) {
        String bar = "";

        if (percentage < 10) {
            bar = BotEmojis.bar1Empty + BotEmojis.bar2Empty + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 20) {
            bar = BotEmojis.bar1Half + BotEmojis.bar2Empty + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 30) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Empty + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 40) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Half + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 50) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2High + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 65) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Full + BotEmojis.bar2Half + BotEmojis.bar3Empty;
        } else if (percentage < 70) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Full + BotEmojis.bar2High + BotEmojis.bar3Empty;
        } else if (percentage < 85) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Full + BotEmojis.bar2Full + BotEmojis.bar3Half;
        } else if (percentage < 101) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Full + BotEmojis.bar2Full + BotEmojis.bar3Full;
        }

        return bar;
    }

    public static int getPercentage(int firstNumber, int secondNumber) {
        double solving = (double) firstNumber/secondNumber;
        solving = solving * 100;
        return (int) solving;
    }

    public static String getEmojiFromBoolean(boolean bool) {
        return (bool ? BotEmojis.check : BotEmojis.xmark);
    }

    public static boolean getSettingBooleanFromNumber(int number) {
        return (number == 100);
    }

    public static boolean makeCheck(SlashCommandEvent event) {
        int test = hasAccount(event);

        if (test == -1) {
            event.reply("Make an account first before using the command.").setEphemeral(true).queue();
            return true;
        }

        return false;
    }

    public static boolean makeCheck(CommandEvent event) {
        int test = hasAccount(event.getAuthor());

        if (test == -1) {
            event.reply("Make an account first before using the command.");
            return true;
        }

        return false;
    }

    public static int hasAccount(SlashCommandEvent event) {
        return hasAccount(event.getUser());
    }

    public static int hasAccount(User user) {
        int test = -1;
        long userId = user.getIdLong();
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT berri FROM Player WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    test = resultSet.getInt("berri");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return test;
    }

    public static Player getPlayer(User user) {
        if (players.containsKey(user)) {
            return players.get(user);
        }
        return new Player(user.getIdLong());
    }
}