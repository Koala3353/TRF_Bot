package com.general_hello.commands.OtherEvents;

import com.general_hello.Bot;
import com.general_hello.commands.Database.SQLiteDataSource;
import com.general_hello.commands.Objects.Level.Level;
import com.general_hello.commands.Objects.ScheduledTask;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Timer;

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

            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS Range (name TEXT, location INTEGER DEFAULT 2);"
                    )) {
                LOGGER.info("Made a new table (Range)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Initializer
            makeTableLevel(Level.PS_LV_125_SURVIVAL);
            makeTableLevel(Level.PS_LV_150_SURVIVAL);
            makeTableLevel(Level.PS_LV_125_FOR_HONOR);
            makeTableLevel(Level.PS_LV_150_FOR_HONOR);
            makeTableLevel(Level.PC_LV_125_FOR_HONOR);
            makeTableLevel(Level.PC_LV_150_FOR_HONOR);
            makeTableLevel(Level.PC_LV_125_SURVIVAL);
            makeTableLevel(Level.PC_LV_150_SURVIVAL);
        } catch (Exception e) {
            LOGGER.error("An error occurred", e);
        }

        LOGGER.info("Setting up the daily task");
        Timer time = new Timer(); // Instantiate Timer Object
        ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
        ZoneId z = ZoneId.of( "America/Montreal" );
        ZonedDateTime now = ZonedDateTime.now( z );
        LocalDate tomorrow = now.toLocalDate().plusDays(1);
        ZonedDateTime tomorrowStart = tomorrow.atStartOfDay( z );
        Duration.between( now , tomorrowStart).toMillis();
        time.schedule(st, Duration.between( now , tomorrowStart).toMillis(), 86400000); // Create Repetitively task for every 1 secs
        LOGGER.info("Started the daily task!");
        LOGGER.info(event.getJDA().getSelfUser().getAsTag() + " is now up and running!");
    }

    private void makeTableLevel(Level level) {
        // Make a new table if it doesn't exist
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("CREATE TABLE IF NOT EXISTS "  + level.getDatabaseName() + " (userid INTEGER, points INTEGER DEFAULT 2000, gamesfoughttoday INTEGER DEFAULT 0);"
                )) {
            LOGGER.info("Made a new table (" + level.getDatabaseName() + ")");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
