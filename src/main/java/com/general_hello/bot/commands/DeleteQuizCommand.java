package com.general_hello.bot.commands;

import com.general_hello.bot.database.SQLiteDataSource;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

public class DeleteQuizCommand extends SlashCommand {
    public DeleteQuizCommand() {
        this.name = "deletequiz";
        this.help = "Deletes a quiz";
        // option is a channel
        this.options = Collections.singletonList(
                new OptionData(OptionType.CHANNEL, "channel", "The quiz's channel to delete")
                        .setRequired(true));
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("DROP TABLE IF EXISTS Quiz" + channel.getId() + ";")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        event.reply("Quiz deleted from database. Feel free to delete the quiz menu in that channel.").setEphemeral(true).queue();
    }
}
