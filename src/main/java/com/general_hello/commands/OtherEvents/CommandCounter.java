package com.general_hello.commands.OtherEvents;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.Database.SQLiteDataSource;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandCounter extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        //code

        if (event.getAuthor().getId().equals("821623701186674718")) return;
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;

        String raw = event.getMessage().getContentRaw().toLowerCase();

        if (raw.contains("cmdcount")) return;

        if (raw.startsWith(DatabaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong()) + " ")) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String[] name = raw.replaceFirst(DatabaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong()) + " ", "").split("\\s+");
            addCount(name[0]);
        } else if (raw.startsWith(DatabaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong()))) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String[] name = raw.replaceFirst(DatabaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong()), "").split("\\s+");
            addCount(name[0]);
        }
    }

    public static int getCount(String name) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT count FROM CommandCount WHERE name = ?")) {

            preparedStatement.setString(1, String.valueOf(name));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void addCount(String name) {
        if (getCount(name) == -1) {
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("INSERT INTO CommandCount" +
                            "(name, count)" +
                            "VALUES (?, 0);"
                    )) {

                preparedStatement.setString(1, String.valueOf(name));

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        int total = (1) + getCount(name);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE CommandCount SET count=? WHERE name=?"
                )) {

            preparedStatement.setString(2, String.valueOf(name));
            preparedStatement.setString(1, String.valueOf(total));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
