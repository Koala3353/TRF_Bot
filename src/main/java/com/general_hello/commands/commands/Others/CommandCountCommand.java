package com.general_hello.commands.commands.Others;

import com.general_hello.commands.Bot;
import com.general_hello.commands.Database.SQLiteDataSource;
import com.general_hello.commands.OtherEvents.CommandCounter;
import com.general_hello.commands.commands.ButtonPaginator;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CommandCountCommand extends Command {
    public CommandCountCommand() {
        this.name = "cmdcount";
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent event) {
        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                .setEventWaiter(Bot.getBot().getEventWaiter())
                .setItemsPerPage(6)
                .setTimeout(5, TimeUnit.MINUTES)
                .useNumberedItems(false);

        ArrayList<String> list = new ArrayList<>();
        int x = 0;
        ArrayList<String> commandNames = getCommandNames();
        while (x < commandNames.size()) {
            list.add("**ignt " + commandNames.get(x) + "** - Was used " + CommandCounter.getCount(commandNames.get(x)) + " times.");
            x++;
        }

        builder.setTitle("Command Usage")
                .setItems(list)
                .addAllowedUsers(event.getAuthor().getIdLong())
                .setColor(Color.decode("#452350"));

        builder.build().paginate(event.getTextChannel(), 1);
    }

    private static ArrayList<String> getCommandNames() {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT name FROM CommandCount")) {

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                ArrayList<String> commandNames = new ArrayList<>();
                while (resultSet.next()) {
                    commandNames.add(resultSet.getString("name"));
                }

                return commandNames;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
