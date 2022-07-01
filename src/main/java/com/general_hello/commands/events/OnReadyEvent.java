package com.general_hello.commands.events;

import com.general_hello.Bot;
import com.general_hello.commands.database.SQLiteDataSource;
import com.general_hello.commands.objects.GlobalVariables;
import com.general_hello.commands.utils.JsonUtils;
import com.general_hello.commands.utils.OddsGetter;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OnReadyEvent extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnReadyEvent.class);

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

            // Make a new UserData table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS UserData ( UserId INTEGER NOT NULL, " +
                            "SolanaAddress TEXT NOT NULL, " +
                            "Ban INTEGER DEFAULT -1);"
                    )) {
                LOGGER.info("Made a new table (UserData)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Make a new Betting table if it doesn't exist
            try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS Betting ( UserId INTEGER NOT NULL, " +
                            "GameId TEXT NOT NULL, " +
                            "TeamNameBet TEXT NOT NULL);"
                    )) {
                LOGGER.info("Made a new table (Betting)");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOGGER.error("An exception was thrown", e);
        }

        LOGGER.info("Retrieving sport keys...");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://odds.p.rapidapi.com/v4/sports?all=true"))
                    .header("X-RapidAPI-Key", GlobalVariables.API_KEY)
                    .header("X-RapidAPI-Host", "odds.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String jsonString = response.body();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String sportKey = jsonObject.getString("key");
                String sportName = jsonObject.getString("title");
                OddsGetter.KEY_OF_SPORT.put(sportName, sportKey);
                LOGGER.debug("Sport name = " + sportName + ", Sport key = " + sportKey);
            }
            LOGGER.info("Successfully retrieved the sport keys! (Enable debug mode to see more details)");
            LOGGER.info("Bot fully initialized and ready.");
        } catch (Exception e) {
            LOGGER.error("Failed to get the data. Shutting the bot down...", e);
            event.getJDA().shutdown();
            LOGGER.info("Shutdown process initialized.");
        }

        File jsonFile = new File("records.json");
        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonUtils.writeJsonTemplate();
        }

        new OddsGetter.GetOddsTask().run();
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new OddsGetter.GetOddsTask(),1, 1, TimeUnit.DAYS);
    }
}