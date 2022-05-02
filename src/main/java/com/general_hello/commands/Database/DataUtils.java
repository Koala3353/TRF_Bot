package com.general_hello.commands.Database;

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
        // Removes all none letter text from the word
        return filterWord.toLowerCase().replace("'", "").replaceAll("\\s+", "");
    }

    public static boolean makeCheck(SlashCommandEvent event) {
        // Checks if the user made an account or not
        int test = hasAccount(event);

        if (test == -1) {
            event.reply("Make an account first before using the command.").setEphemeral(true).queue();
            return true;
        }

        return false;
    }

    public static boolean makeCheck(CommandEvent event) {
        // Checks if the user made an account or not
        int test = hasAccount(event.getAuthor());

        if (test == -1) {
            event.reply("Make an account first before using the command.");
            return true;
        }

        return false;
    }

    // Checks if the user made an account or not
    public static int hasAccount(SlashCommandEvent event) {
        return hasAccount(event.getUser());
    }

    // Checks if the user made an account or not
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

    // Gets the player from the hashmap, if it isn't there it'll make a new player object
    public static Player getPlayer(User user) {
        if (players.containsKey(user)) {
            return players.get(user);
        }
        return new Player(user.getIdLong());
    }
}