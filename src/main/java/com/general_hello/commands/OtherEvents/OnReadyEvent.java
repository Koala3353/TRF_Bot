package com.general_hello.commands.OtherEvents;

import com.general_hello.Bot;
import com.general_hello.Config;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Database.SQLiteDataSource;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Others.Achievement;
import com.general_hello.commands.Objects.Items.Object;
import com.general_hello.commands.Utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class OnReadyEvent extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnReadyEvent.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        try {
            Bot.jda = event.getJDA();
            LOGGER.info("Starting the bot...");
            try {
                final File dbFile = new File("database.db");

                // Create the database file if it doesn't exist
                if (!dbFile.exists()) {
                    if (dbFile.createNewFile()) {
                        LOGGER.info("Created database file");
                    } else {
                        LOGGER.info("Could not create database file");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                SQLiteDataSource.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            } catch (Exception e) {
                e.printStackTrace();
            }

            LOGGER.info("Opened database successfully");

            // Make a new Player table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("""
                                    CREATE TABLE IF NOT EXISTS Player (
                                    userId INTEGER,
                                    hp	INTEGER DEFAULT 100,
                                    melee	INTEGER DEFAULT 500,
                                    magic	INTEGER DEFAULT 0,
                                    neoDevilFruit	INTEGER DEFAULT 0,
                                    professionExp	INTEGER DEFAULT 0,
                                    strength	INTEGER DEFAULT 500,
                                    endurance	INTEGER DEFAULT 500,
                                    intelligence	INTEGER DEFAULT 500,
                                    willpower	INTEGER DEFAULT 500,
                                    speed	INTEGER DEFAULT 500,
                                    rank	INTEGER DEFAULT 1,
                                    exp	INTEGER DEFAULT 1,
                                    aiDefeated	INTEGER DEFAULT 0,
                                    achievementTitle	TEXT DEFAULT 'NONE',
                                 berri	INTEGER DEFAULT 0,
                                 rainbowShards	INTEGER DEFAULT 0,
                                 rainbowShardsBought	INTEGER DEFAULT 0,
                                 skillSlotsCap	INTEGER DEFAULT 4,
                                 pvpWon	INTEGER DEFAULT 0,
                                 pvpLost	INTEGER DEFAULT 0,
                                 rankWins	INTEGER DEFAULT 0,
                                 rankLoss	INTEGER DEFAULT 0,
                                 bounty	INTEGER DEFAULT '1/-1',
                                 likes	INTEGER DEFAULT 0,
                                 marriagePartnerId	INTEGER DEFAULT -1,
                                 senseiId	INTEGER DEFAULT -1,
                                 level	INTEGER DEFAULT 0,
                                 profession	TEXT DEFAULT 'NONE'
                            );"""
                    )) {
                LOGGER.info("Made a new table (Player)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Make a new Skills table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS Skills (" +
                            "userId INTEGER," + skills() +
                            ");"
                    )) {
                LOGGER.info("Made a new table (Skills)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Make a new Achievement table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS Achievement (" +
                            "userId INTEGER," + achievements() +
                            ");"
                    )) {
                LOGGER.info("Made a new table (Achievement)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOGGER.error("An exception was thrown", e);
        }
    }

    private String skills() {
        // Gets the skills and transform it to SQLite text
        StringBuilder items = new StringBuilder();

        int x = 0;
        ArrayList<String> names = Initializer.allNames;
        while (x < names.size()) {
            Object object = Initializer.allObjects.get(names.get(x));
            items.append(DataUtils.filter(object.getName())).append(" INTEGER DEFAULT 0");
            if (x + 1 < names.size()) {
                items.append(", ");
            }
            x++;
        }

        return items.toString();
    }

    private String achievements() {
        // Gets the achievements and transform it to SQLite text
        StringBuilder items = new StringBuilder();

        int x = 0;
        ArrayList<String> names = Initializer.allAchievementNames;
        while (x < names.size()) {
            Achievement object = Initializer.achievements.get(x);
            items.append(DataUtils.filter(object.getName())).append(" INTEGER DEFAULT 0");
            if (x + 1 < names.size()) {
                items.append(", ");
            }
            x++;
        }

        return items.toString();
    }

    public static HashMap<Long, User> messageWithPing = new HashMap<>();

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        // If a message was deleted and the user pinged @Xraze it'll send the message notifying @Xraze
        try {
            if (messageWithPing.containsKey(event.getMessageIdLong())) {
                User user = messageWithPing.get(event.getMessageIdLong());
                if (String.valueOf(event.getGuild().getIdLong()).equals(Config.get("guild"))) {
                    event.getChannel().sendMessage(user.getAsMention() + ", you just pinged " +
                            event.getJDA().getUserById(Config.get("owner_id")).getAsMention()).queue();
                }
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        // If a message was edited and the user pinged @Xraze it'll send the message notifying @Xraze
        try {
            if (messageWithPing.containsKey(event.getMessageIdLong())) {
                User user = messageWithPing.get(event.getMessageIdLong());
                if (String.valueOf(event.getGuild().getIdLong()).equals(Config.get("guild"))) {
                    event.getChannel().sendMessage(user.getAsMention() + ", you just pinged " +
                            event.getJDA().getUserById(Config.get("owner_id")).getAsMention()).queue();
                }
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Stores the message if the user pinged @Xraze since the deleted message event doesn't show the content of the deleted message
        if (!event.getGuild().getId().equals(Config.get("guild"))) return;
        try {
            if (!event.getMessage().getMentionedUsers().isEmpty()) {
                if (event.getMessage().getMentionedUsers().contains(event.getJDA().getUserById(Config.get("owner_id")))) {
                    messageWithPing.put(event.getMessageIdLong(), event.getAuthor());
                }
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        // Checks if it's the main server that the event occurred at
        if (!event.getGuild().getId().equals(Config.get("guild"))) return;
        // If the roles is one of the premium roles it'll send a message to the channel specified in the .env file
        TextChannel logs = event.getGuild().getTextChannelById(Config.get("logs"));
        if (event.getRoles().contains(event.getGuild().getRoleById(Config.get("premium")))) {
            OffsetDateTime timeNow = OffsetDateTime.now();
            timeNow = timeNow.plusMonths(1);
            String relativeTime = TimeFormat.RELATIVE.format(timeNow);
            String longTime = TimeFormat.DATE_TIME_LONG.format(timeNow);
            logs.sendMessageEmbeds(EmbedUtil.defaultEmbed(
                    "**" + event.getUser().getAsTag() + " (" + event.getUser().getId() + ")** - " +
                            "**PayPal Premium Pass** expires at: " + relativeTime + " (" + longTime + ")")).queue();
        }
        // If the roles is one of the patreon roles it'll send a message to the channel specified in the .env file
        for (Role role : event.getRoles()) {
            if (Bot.patreonRoles.contains(role.getIdLong())) {
                OffsetDateTime timeNow = OffsetDateTime.now();
                timeNow = timeNow.plusMonths(1);
                String relativeTime = TimeFormat.RELATIVE.format(timeNow);
                String longTime = TimeFormat.DATE_TIME_LONG.format(timeNow);
                logs.sendMessageEmbeds(EmbedUtil.defaultEmbed(
                        "**" + event.getUser().getAsTag() + " (" + event.getUser().getId() + ")** - " +
                                "**Patreon** (" + role.getAsMention() + ") expires at: " + relativeTime + " (" + longTime + ")")).queue();
            }
        }
    }
}
