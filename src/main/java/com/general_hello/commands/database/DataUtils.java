package com.general_hello.commands.database;

import com.general_hello.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public static boolean hasAccount(long userId) {
        return getSolanaAddress(userId) != null;
    }

    public static String getSolanaAddress(long userId) {
        LOGGER.info("Made a request to get the Solana Address of " + userId + ".");
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT SolanaAddress FROM UserData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("SolanaAddress");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isBanned(long userId) {
        LOGGER.info("Made a request to get if the user (" + userId + ") is banned or not.");
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Ban FROM UserData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Ban") != -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setBan(long userId, boolean isBan) {
        LOGGER.info("Made a request to " + (!isBan ? "remove the ban of" : "ban") + " (" + userId + ") from the bot.");
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE UserData SET Ban=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf((isBan ? 1 : -1)));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void newAccount(long userId, String solanaAddress) {
        LOGGER.info("Made a new info for " + solanaAddress + " with the user id of " + userId + ".");
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO UserData" +
                        "(UserId, SolanaAddress)" +
                        "VALUES (?, ?);")) {

            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, solanaAddress);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean newBet(long userId, String teamName, String gameID) {
        LOGGER.info("Made a new bet for " + teamName + " by the user id of " + userId + ".");
        if (getBetTeam(userId, gameID) != null) {
            LOGGER.info("The user already has a bet for this game.");
            return false;
        }
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Betting" +
                        "(UserId, GameId, TeamNameBet)" +
                        "VALUES (?, ?, ?);")) {

            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, gameID);
            preparedStatement.setString(3, teamName);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean newFollower(long userId, long champ) {
        LOGGER.info("Made a new follower for " + champ + " by the user id of " + userId + ".");
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Follow" +
                        "(UserId, ChampId)" +
                        "VALUES (?, ?);")) {

            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, String.valueOf(champ));

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean newChampMessage(long messageId, long champ) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Follow" +
                        "(UserId, ChampId)" +
                        "VALUES (?, ?);")) {

            preparedStatement.setString(1, String.valueOf(messageId));
            preparedStatement.setString(2, String.valueOf(champ));

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long getAuthorOfPost(long messageId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT ChampId FROM Follow WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(messageId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("ChampId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getBetTeam(long userId, String gameId) {
        LOGGER.info("Made a request to get the bet of " + userId + " in the game " + gameId + ".");
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT TeamNameBet FROM Betting WHERE UserId = ? AND GameId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, gameId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("TeamNameBet");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Long> getFollowersOfChamp(long champ) {
        LOGGER.info("Made a request to get the users who followed " + champ + ".");
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT UserId FROM Follow WHERE ChampId = ?")) {

            preparedStatement.setString(1, String.valueOf(champ));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Long> users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(resultSet.getLong("UserId"));
                }
                return users;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Long> getUsers(String gameId) {
        LOGGER.info("Made a request to get the users who placed a bet in the game " + gameId + ".");
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT UserId FROM Betting WHERE GameId = ?")) {

            preparedStatement.setString(1, gameId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Long> users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(resultSet.getLong("UserId"));
                }
                return users;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verifyAddress(String address) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://public-api.solscan.io/token/meta?tokenAddress=" + address))
                    .build();

            HttpResponse<String> response;
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 400) {
                return false;
            } else if (response.statusCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to make a request", e);
        }

        return false;
    }
}