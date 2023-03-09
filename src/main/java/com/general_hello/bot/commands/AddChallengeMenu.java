package com.general_hello.bot.commands;

import com.general_hello.bot.database.SQLiteDataSource;
import com.jagrosh.jdautilities.command.MessageContextMenu;
import com.jagrosh.jdautilities.command.MessageContextMenuEvent;
import net.dv8tion.jda.api.Permission;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddChallengeMenu extends MessageContextMenu {
    public AddChallengeMenu() {
        this.name = "Add Challenge";
        Permission[] permissions = new Permission[1];
        permissions[0] = Permission.MESSAGE_MANAGE;
        this.userPermissions = permissions;
    }

    @Override
    protected void execute(MessageContextMenuEvent event) {
        long messageId = event.getInteraction().getTarget().getIdLong();

        // Make a new Challenge table if it doesn't exist
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("CREATE TABLE IF NOT EXISTS Challenge" + messageId + " ( UserId INTEGER NOT NULL);")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            event.reply("Error creating database table!").setEphemeral(true).queue();
        }

        event.reply("Challenge added!").setEphemeral(true).queue();
    }
}