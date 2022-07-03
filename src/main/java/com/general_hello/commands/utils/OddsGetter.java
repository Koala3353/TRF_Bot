package com.general_hello.commands.utils;

import com.general_hello.Bot;
import com.general_hello.commands.objects.Game;
import com.general_hello.commands.objects.GlobalVariables;
import com.general_hello.commands.objects.SportType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;
import java.util.*;

public class OddsGetter {
    /**
     * <b>Sport name</b> as value, then <b>sport key</b> as key
     */
    public static Map<String, String> KEY_OF_SPORT = new HashMap<>();
    public static Map<String, Game> gameIdToGame = new HashMap<>();
    public static List<Game> games = new ArrayList<>();
    public static Map<String, Long> gameIdToMessageId = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(OddsGetter.class);

    public static void getOddsAndSave() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://odds.p.rapidapi.com/v4/sports/upcoming/odds?regions=us&oddsFormat=decimal&markets=h2h&dateFormat=unix"))
                    .header("X-RapidAPI-Key", GlobalVariables.API_KEY)
                    .header("X-RapidAPI-Host", "odds.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String sportTitle = jsonObject.getString("sport_title");
                if (SportType.sportExists(sportTitle)) {
                    JSONArray bookmakers = jsonObject.getJSONArray("bookmakers");
                    for (int j = 0; j < bookmakers.length(); j++) {
                        JSONObject market = bookmakers.getJSONObject(j);
                        if (market.getString("title").equals(GlobalVariables.BOOKMAKERS)) {
                            JSONArray markets = market.getJSONArray("markets");
                            for (int a = 0; a < markets.length(); a++) {
                                if (markets.getJSONObject(a).getString("key").equals("h2h")) {
                                    JSONArray outcomes = markets.getJSONObject(a).getJSONArray("outcomes");
                                    JSONObject outcome = outcomes.getJSONObject(0);
                                    double homePrice = outcome.getDouble("price");
                                    outcome = outcomes.getJSONObject(1);
                                    double awayPrice = outcome.getDouble("price");
                                    long commence_time = jsonObject.getLong("commence_time");
                                    if (commence_time > Instant.now().getEpochSecond()) {
                                        Game game = new Game(SportType.getSportTypeFromSport(sportTitle),
                                                commence_time, jsonObject.getString("home_team"),
                                                jsonObject.getString("away_team"), jsonObject.getString("id"), sportTitle,
                                                homePrice, awayPrice);
                                        saveGame(game);
                                        LOGGER.info("Game saved: " + game);
                                    } else {
                                        LOGGER.info("Game is in the past: " + jsonObject.getString("id"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Game> getOdds() {
        List<Game> gamesList = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://odds.p.rapidapi.com/v4/sports/upcoming/odds?regions=us&oddsFormat=decimal&markets=h2h&dateFormat=unix"))
                    .header("X-RapidAPI-Key", GlobalVariables.API_KEY)
                    .header("X-RapidAPI-Host", "odds.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String sportTitle = jsonObject.getString("sport_title");
                if (SportType.sportExists(sportTitle)) {
                    JSONArray bookmakers = jsonObject.getJSONArray("bookmakers");
                    for (int j = 0; j < bookmakers.length(); j++) {
                        JSONObject market = bookmakers.getJSONObject(j);
                        if (market.getString("title").equals(GlobalVariables.BOOKMAKERS)) {
                            JSONArray markets = market.getJSONArray("markets");
                            for (int a = 0; a < markets.length(); a++) {
                                if (markets.getJSONObject(a).getString("key").equals("h2h")) {
                                    JSONArray outcomes = markets.getJSONObject(a).getJSONArray("outcomes");
                                    JSONObject outcome = outcomes.getJSONObject(0);
                                    double homePrice = outcome.getDouble("price");
                                    outcome = outcomes.getJSONObject(1);
                                    double awayPrice = outcome.getDouble("price");
                                    long commence_time = jsonObject.getLong("commence_time");
                                    if (commence_time > Instant.now().getEpochSecond()) {
                                        Game game = new Game(SportType.getSportTypeFromSport(sportTitle),
                                                commence_time, jsonObject.getString("home_team"),
                                                jsonObject.getString("away_team"), jsonObject.getString("id"), sportTitle,
                                                homePrice, awayPrice, true);
                                        LOGGER.info("Game saved: " + game);
                                        gamesList.add(game);
                                    } else {
                                        LOGGER.info("Game is in the past: " + jsonObject.getString("id"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gamesList;
    }

    public static void saveGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game is null");
        }
        games.add(game);
        gameIdToGame.put(game.getId(), game);
    }

    public static Result getOdds(String sportKey, String gameId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://odds.p.rapidapi.com/v4/sports/" + sportKey + "/odds?regions=us&oddsFormat=decimal&markets=h2h&dateFormat=unix"))
                    .header("X-RapidAPI-Key", GlobalVariables.API_KEY)
                    .header("X-RapidAPI-Host", "odds.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                if (id.equals(gameId)) {
                    JSONArray bookmakers = jsonObject.getJSONArray("bookmakers");
                    for (int j = 0; j < bookmakers.length(); j++) {
                        JSONObject market = bookmakers.getJSONObject(j);
                        if (market.getString("title").equals(GlobalVariables.BOOKMAKERS)) {
                            JSONArray markets = market.getJSONArray("markets");
                            for (int a = 0; a < markets.length(); a++) {
                                if (markets.getJSONObject(a).getString("key").equals("h2h")) {
                                    JSONArray outcomes = markets.getJSONObject(a).getJSONArray("outcomes");
                                    JSONObject outcome = outcomes.getJSONObject(0);
                                    double homePrice = outcome.getDouble("price");
                                    outcome = outcomes.getJSONObject(1);
                                    double awayPrice = outcome.getDouble("price");
                                    return new Result(homePrice, awayPrice);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class GetOddsTask extends TimerTask {
        @Override
        public void run() {
            if (games.isEmpty()) {
                try {
                    LOGGER.info("Starting the general get odds task");
                    games = new ArrayList<>();
                    gameIdToGame = new HashMap<>();
                    getOddsAndSave();
                    LOGGER.info("Done running the general odds retrieving task. " + games.size() + " games were retrieved");
                } catch (Exception ex) {
                    System.out.println("error running thread GetOddsTask" + ex.getMessage());
                }
            } else {
                getOdds().forEach(game -> {
                    if (!gameIdToGame.containsKey(game.getId())) {
                        Game newGame = new Game(game.getSportType(), game.getGameTime(), game.getHomeTeam(), game.getAwayTeam(), game.getId(), game.getSportKey(), game.getHomePrice(), game.getAwayPrice());
                        games.add(newGame);
                        gameIdToGame.put(newGame.getId(), newGame);
                    }
                });
            }
        }
    }

    public static class GetOddsSpecificTask extends TimerTask {
        private final String sportKey;
        private final String gameId;

        public GetOddsSpecificTask(String sportKey, String gameId) {
            this.sportKey = sportKey;
            this.gameId = gameId;
        }

        @Override
        public void run() {
            try {
                LOGGER.info("Updating the odds for " + gameId + " with the sport key of " + sportKey);
                Result result = getOdds(sportKey, gameId);
                Game newGame = findAndEdit(gameId, result);
                EmbedBuilder embed = getEmbed(newGame);
                boolean lock = newGame.getEditCount() >= 5;
                long textChannelId = newGame.getSportType().getChannelId();
                MessageChannel channel = Bot.getJda().getTextChannelById(textChannelId);
                if (gameIdToMessageId.containsKey(gameId)) {
                    long messageId = gameIdToMessageId.get(gameId);
                    LOGGER.info("Updating the message with id " + messageId);
                    channel.retrieveMessageById(messageId).queue((message) -> {
                        message.editMessageEmbeds(embed.build())
                                .setActionRow(Button.secondary(
                                                "0000:bet:" + newGame.getHomeTeam() + ":" + newGame.getId(),
                                                newGame.getHomeTeam()).withDisabled(lock),
                                        Button.secondary("0000:bet:" + newGame.getAwayTeam() + ":" + newGame.getId(),
                                                newGame.getAwayTeam()).withDisabled(lock)).queue();
                    });
                } else {
                    LOGGER.info("Creating a new message");
                    channel.sendMessageEmbeds(embed.build())
                            .setActionRow(Button.secondary(
                                    "0000:bet:" + newGame.getHomeTeam() + ":" + newGame.getId(),
                                            newGame.getHomeTeam()).withDisabled(lock),
                                    Button.secondary("0000:bet:" + newGame.getAwayTeam() + ":" + newGame.getId(),
                                            newGame.getAwayTeam()).withDisabled(lock))
                            .queue((message) -> {
                                gameIdToMessageId.put(gameId, message.getIdLong());
                            });
                }
            } catch (Exception ex) {
                System.out.println("error running thread " + ex.getMessage());
            }
        }
    }

    private static EmbedBuilder getEmbed(Game game) {
        int timesUpdated = game.getEditCount();
        Color color = Color.RED;
        boolean lastCall = false;
        if (timesUpdated == 1) {
            color = Color.YELLOW;
        } else if (timesUpdated == 2) {
            color = Color.ORANGE;
        } else if (timesUpdated == 3) {
            color = Color.CYAN;
        } else if (timesUpdated == 4) {
            color = Color.GREEN;
        } else {
            lastCall = true;
        }
        game.addEditCount();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(game.getHomeTeam() + " vs " + game.getAwayTeam());
        embed.setColor(color);
        SelfUser selfUser = Bot.getJda().getSelfUser();
        embed.setFooter(selfUser.getName(), selfUser.getEffectiveAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.addField(game.getHomeTeam(), String.format("%.2f", game.getHomePrice()), true);
        embed.addField(game.getAwayTeam(), String.format("%.2f", game.getAwayPrice()), true);
        embed.setDescription("Game ID: **" + game.getId() + "**\n" +
                (lastCall ? "Game started " : "Game starts ") + TimeFormat.RELATIVE.format(game.getGameTime()*1000) +
                " (" + TimeFormat.DATE_TIME_SHORT.format(game.getGameTime()*1000)+ ")\n" +
                "Sport: " + game.getSport() + " (" + game.getSportType().getName() + ")");
        return embed;
    }

    public static EmbedBuilder getEmbedPersonal(Game game) {
        Color color = Color.CYAN;
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(game.getHomeTeam() + " vs " + game.getAwayTeam());
        embed.setDescription(game.getSport());
        embed.setColor(color);
        SelfUser selfUser = Bot.getJda().getSelfUser();
        embed.setFooter(selfUser.getName(), selfUser.getEffectiveAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.addField(game.getHomeTeam(), String.format("%.2f", game.getHomePrice()), true);
        embed.addField(game.getAwayTeam(), String.format("%.2f", game.getAwayPrice()), true);
        embed.setDescription("Game ID: **" + game.getId() + "**\n" +
                "Game starts " + TimeFormat.RELATIVE.format(game.getGameTime()*1000) +
                " (" + TimeFormat.DATE_TIME_SHORT.format(game.getGameTime()*1000)+ ")\n" +
                "Sport: " + game.getSport() + " (" + game.getSportType().getName() + ")");
        return embed;
    }

    public static Game findAndEdit(String gameId, Result result) {
        Game game = gameIdToGame.get(gameId);
        game.setHomePrice(result.getHomePrice());
        LOGGER.info("Updating the info of the game with the id of " + gameId);
        return game.setAwayPrice(result.getAwayPrice());
    }

    record Result(double homePrice, double awayPrice) {

        public double getHomePrice() {
            return homePrice;
        }

        public double getAwayPrice() {
            return awayPrice;
        }
    }
}
