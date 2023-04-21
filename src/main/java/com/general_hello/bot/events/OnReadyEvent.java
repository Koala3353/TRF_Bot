package com.general_hello.bot.events;

import com.general_hello.Bot;
import com.general_hello.bot.database.SQLiteDataSource;
import com.general_hello.bot.objects.tasks.whop.CancellingWhopMemberTask;
import com.general_hello.bot.objects.tasks.eod.EODTask;
import com.general_hello.bot.objects.tasks.eod.EODTaskReminder;
import com.general_hello.bot.objects.tasks.whop.NewWhopMemberTask;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class OnReadyEvent extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnReadyEvent.class);
    public static ScheduledExecutorService dailyLbTask = Executors.newScheduledThreadPool(1);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        try {
            Bot.setJda(event.getJDA());
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

            // Make a new EODReport table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS EODReport ( UserId INTEGER NOT NULL, " +
                            "Relapse INTEGER DEFAULT -1, " +
                            "Color TEXT DEFAULT 'NA'," +
                            "Urge INTEGER DEFAULT -1, " +
                            "LastAnsweredTime TEXT DEFAULT '2000-01-01T00:00:01+01:00'," +
                            "DidAnswer INTEGER DEFAULT 0);"
                    )) {
                LOGGER.info("Loaded a table (EODReport)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Make a new EODReportDiscordBot table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS EODReportDiscordBot ( UserId INTEGER NOT NULL, " +
                            "ReportStreak INTEGER DEFAULT 0, " +
                            "RelapseFreeStreak INTEGER DEFAULT 0," +
                            "IsEODAnswered INTEGER DEFAULT 0, " +
                            "BestRelapseFreeStreak INTEGER DEFAULT 0," +
                            "EXPVC INTEGER DEFAULT 0," +
                            "EXPTEXT INTEGER DEFAULT 0," +
                            "AccEXP INTEGER DEFAULT 0," +
                            "BestReportStreak INTEGER DEFAULT 0," +
                            "MissedDays INTEGER DEFAULT 0," +
                            "RelapseCount INTEGER DEFAULT 0," +
                            "TotalUrge INTEGER DEFAULT 0," +
                            "TotalAnswered INTEGER DEFAULT 0," +
                            "RedCount INTEGER DEFAULT 0," +
                            "YellowCount INTEGER DEFAULT 0, " +
                            "GreenCount INTEGER DEFAULT 0);"
                    )) {
                LOGGER.info("Loaded a table (EODReportDiscordBot)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Make a new Questions table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS Questions ( UserId INTEGER NOT NULL, " +
                            "Question TEXT DEFAULT 'None');"
                    )) {
                LOGGER.info("Loaded a table (Questions)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Make a new NewUser table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS NewUser ( UserId INTEGER NOT NULL, " +
                            "IsNew INTEGER DEFAULT 1);"
                    )) {
                LOGGER.info("Loaded a table (NewUser)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS SecondUser ( UserId INTEGER NOT NULL, " +
                            "IsNew INTEGER DEFAULT 1);"
                    )) {
                LOGGER.info("Loaded a table (SecondUser)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS Roles ( " +
                            "RoleId INTEGER NOT NULL," +
                            "RequiredRoles TEXT NOT NULL" +
                            ");")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS SelectRoles ( " +
                            "RoleId INTEGER NOT NULL," +
                            "Count INTEGER DEFAULT 0" +
                            ");")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS AirtableUser ( " +
                            "UserId INTEGER NOT NULL," +
                            "RecordId TEXT NOT NULL," +
                            "OverviewId TEXT DEFAULT 'None'" +
                            ");")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the newWhopMemberTask task
        Timer timer = new Timer(true);
        NewWhopMemberTask newWhopMemberTask = new NewWhopMemberTask();
        timer.scheduleAtFixedRate(newWhopMemberTask, 0L, 60000);

        // Start the cancellingWhopMemberTask task
        timer = new Timer(true);
        CancellingWhopMemberTask cancellingWhopMemberTask = new CancellingWhopMemberTask();
        timer.scheduleAtFixedRate(cancellingWhopMemberTask, 0L, 60000);

        // Start the daily EOD report sending
        timer = new Timer(true);
        EODTask eodTask = new EODTask(1062100366105268327L);

        // global time
        OffsetDateTime oneDayLater = OffsetDateTime.now(ZoneId.of("UTC-6")).withHour(0).withMinute(0).withSecond(0);
        // check if onedaylater is before now
        if (oneDayLater.isBefore(OffsetDateTime.now(ZoneId.of("UTC-6")))) {
            oneDayLater = oneDayLater.plusDays(1);
        }
        timer.scheduleAtFixedRate(eodTask, Date.from(oneDayLater.toInstant()), 86400000);

        // 90 min reminder
        timer = new Timer(true);
        EODTaskReminder eodTaskReminder = new EODTaskReminder(1062100366105268327L);

        // global time
        oneDayLater = OffsetDateTime.now(ZoneId.of("UTC-6")).withHour(0).withMinute(0).withSecond(0);
        // check if onedaylater is before now
        if (oneDayLater.isBefore(OffsetDateTime.now(ZoneId.of("UTC-6")))) {
            oneDayLater = oneDayLater.plusDays(1);
        }
        oneDayLater = oneDayLater.minusHours(1).minusMinutes(30);
        timer.scheduleAtFixedRate(eodTaskReminder, Date.from(oneDayLater.toInstant()), 86400000);

        // 30 min reminder
        timer = new Timer(true);
        eodTaskReminder = new EODTaskReminder(1062100366105268327L);

        // global time
        oneDayLater = OffsetDateTime.now(ZoneId.of("UTC-6")).withHour(0).withMinute(0).withSecond(0);
        // check if onedaylater is before now
        if (oneDayLater.isBefore(OffsetDateTime.now(ZoneId.of("UTC-6")))) {
            oneDayLater = oneDayLater.plusDays(1);
        }
        oneDayLater = oneDayLater.minusMinutes(30);
        timer.scheduleAtFixedRate(eodTaskReminder, Date.from(oneDayLater.toInstant()), 86400000);

//        Airtable.smth();
    }
}