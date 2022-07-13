package com.general_hello.bot.database;

import com.general_hello.Bot;
import com.general_hello.bot.objects.SpecialPost;
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

    public static synchronized String getSolanaAddress(long userId) {
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

    public static synchronized boolean isBanned(long userId) {
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

    public static synchronized void setBan(long userId, boolean isBan) {
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

    public static synchronized void newAccount(long userId, String solanaAddress) {
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

    public static synchronized boolean newBet(long userId, String teamName, String gameID) {
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

    public static synchronized void newFollower(long userId, long champ) {
        LOGGER.info("Made a new follower for " + champ + " by the user id of " + userId + ".");
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Follow" +
                        "(UserId, ChampId)" +
                        "VALUES (?, ?);")) {

            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, String.valueOf(champ));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void removeFollower(long userId, long champ) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("DELETE FROM Follow WHERE UserId=? AND ChampId=?")) {

            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, String.valueOf(champ));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void newChampMessage(long messageId, long champ) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO PostInteractions" +
                        "(PostId, UserId)" +
                        "VALUES (?, ?);")) {

            preparedStatement.setString(1, String.valueOf(messageId));
            preparedStatement.setString(2, String.valueOf(champ));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean hasInteracted(long userId, long messageId) {
        int hasInteracted = -1;
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Interacted FROM HasInteracted WHERE PostId = ? AND UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(messageId));
            preparedStatement.setString(2, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hasInteracted = resultSet.getInt("Interacted");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasInteracted == 1;
    }

    public static synchronized List<SpecialPost> getSpecialPosts() {
        List<SpecialPost> posts = new ArrayList<>();
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT UnixTimePost FROM SpecialPosts")) {

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long unixTimePost = resultSet.getLong("UnixTimePost");
                    int interactionCount = getInteractionCountFromTime(unixTimePost);
                    posts.add(new SpecialPost(unixTimePost, interactionCount));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public static synchronized String getContent(long unixTime) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Content FROM SpecialPosts WHERE UnixTimePost = ?")) {
            preparedStatement.setString(1, String.valueOf(unixTime));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Content");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static synchronized String getPoster(long unixTime) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT PosterId FROM SpecialPosts WHERE UnixTimePost = ?")) {
            preparedStatement.setString(1, String.valueOf(unixTime));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("PosterId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static synchronized int getHofCount(long userId) {
        int count = 0;
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT IsTopPost FROM SpecialPosts WHERE PosterId = ?")) {
            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    if (resultSet.getInt("IsTopPost") == 1) {
                        count++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static synchronized int getHighInteractionPosts(long userId) {
        int count = 0;
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT InteractionCount FROM SpecialPosts WHERE PosterId = ?")) {
            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    if (resultSet.getInt("InteractionCount") >= 10) {
                        count++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static synchronized int getInteractionCountFromTime(long unixTime) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT InteractionCount FROM SpecialPosts WHERE UnixTimePost = ?")) {

            preparedStatement.setString(1, String.valueOf(unixTime));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("InteractionCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static synchronized void newSpecialPost(long unixTimeOfPost, long channelId, String channelName,
                                                   long posterId, String posterName, String postContent, int interactionCount) {
        if (isSpecialPostSaved(unixTimeOfPost)) {
            return;
        }
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO SpecialPosts" +
                        "(UnixTimePost, ChannelId, ChannelName, PosterId, PosterName, Content, InteractionCount)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?);")) {
            preparedStatement.setString(1, String.valueOf(unixTimeOfPost));
            preparedStatement.setString(2, String.valueOf(channelId));
            preparedStatement.setString(3, channelName);
            preparedStatement.setString(4, String.valueOf(posterId));
            preparedStatement.setString(5, posterName);
            preparedStatement.setString(6, postContent);
            preparedStatement.setString(7, String.valueOf(interactionCount));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setTopPost(long unixTimePost) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE SpecialPosts SET IsTopPost=? WHERE UnixTimePost=?"
                )) {

            preparedStatement.setString(2, String.valueOf(unixTimePost));
            preparedStatement.setString(1, "1");

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static synchronized boolean isSpecialPostSaved(long unixTimePost) {
        int hasInteracted = -1;
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT InteractionCount FROM SpecialPosts WHERE UnixTimePost = ?")) {

            preparedStatement.setString(1, String.valueOf(unixTimePost));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hasInteracted = resultSet.getInt("InteractionCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasInteracted != -1;
    }

    public static synchronized void newInteractedUser(long messageId, long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO HasInteracted" +
                        "(UserId, PostId)" +
                        "VALUES (?, ?);")) {
            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, String.valueOf(messageId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized long getChampTime(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Time FROM ChampTime WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("Time");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static synchronized void newChampTime(long timeUnix, long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO ChampTime" +
                        "(UserId, Time)" +
                        "VALUES (?, ?);")) {
            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, String.valueOf(timeUnix));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized long getAuthorOfPost(long messageId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT UserId FROM PostInteractions WHERE PostId = ?")) {

            preparedStatement.setString(1, String.valueOf(messageId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("UserId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static synchronized void addInteraction(long postId) {
        int incrementInteraction = getInteractionCount(postId) + 1;
        LOGGER.info("Made a request to add an interaction to the post (" + postId + ").");
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE PostInteractions SET Interaction=? WHERE PostId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(incrementInteraction));
            preparedStatement.setString(1, String.valueOf(postId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getInteractionCount(long postId) {
        LOGGER.info("Made a request to get the interaction count of the post (" + postId + ").");
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Interaction FROM PostInteractions WHERE PostId = ?")) {

            preparedStatement.setString(1, String.valueOf(postId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Interaction");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static synchronized String getBetTeam(long userId, String gameId) {
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

    public static synchronized List<Long> getFollowersOfChamp(long champ) {
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

    public static synchronized List<Long> getChamps() {
        LOGGER.info("Made a request to get the champs");
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT UserId FROM PostInteractions")) {

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

    public static synchronized List<Long> getUsers(String gameId) {
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

    public static synchronized boolean verifyAddress(String address) {
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