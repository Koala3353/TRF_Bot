package com.general_hello.bot.database;

import com.general_hello.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public static synchronized int getRelapse(long userId) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Relapse FROM EODReport WHERE UserId = ?")) {
            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Relapse");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static synchronized int didAnswer(long userId) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT DidAnswer FROM EODReport WHERE UserId = ?")) {
            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("DidAnswer");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Boolean getBooleanFromInt(int theInt) {
        if (theInt == 1) {
            return true;
        } else if (theInt == 0) {
            return false;
        } else {
            return null;
        }
    }
    public static synchronized String getColor(long userId) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Color FROM EODReport WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Color");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized List<Long> getUsers() {
        List<Long> userIds = new ArrayList<>();
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT UserId FROM EODReport")) {

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    userIds.add(resultSet.getLong("UserId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIds;
    }

    public static synchronized String getColorFancyText(String colorRaw) {
        if (colorRaw == null) {
            return "Unknown";
        }
        return switch (colorRaw) {
            case "Red" -> "Red";
            case "Yellow" -> "Yellow";
            case "Green" -> "Green";
            default -> "Unknown";
        };
    }

    public static synchronized Color getColorFromPlainText(String colorRaw) {
        if (colorRaw == null) {
            return Color.BLACK;
        }

        return switch (colorRaw) {
            case "Red" -> Color.RED;
            case "Yellow" -> Color.YELLOW;
            case "Green" -> Color.GREEN;
            default -> Color.BLACK;
        };
    }

    public static synchronized int getUrge(long userId) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Urge FROM EODReport WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Urge");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static synchronized String getLastAnsweredTime(long userId) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT LastAnsweredTime FROM EODReport WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("LastAnsweredTime");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "2000-01-01T00:00:01+01:00";
    }

    public static OffsetDateTime getOffsetDateTime(String time) {
        return OffsetDateTime.parse(time);
    }

    public static synchronized void setLastAnsweredTime(long userId, OffsetDateTime time) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReport SET LastAnsweredTime=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, time.toString());
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setLastAnsweredTime(long userId, String time) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReport SET LastAnsweredTime=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, time);
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setRelapse(long userId, boolean didRelapse) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReport SET Relapse=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, didRelapse ? "1" : "0");
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setDidAnswer(long userId, boolean didAnswer) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReport SET DidAnswer=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, didAnswer ? "1" : "0");
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setRelapse(long userId, int didRelapse) {
        // Checks if a database entry has been made
        makeCheck(userId);
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReport SET Relapse=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(didRelapse));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setColor(long userId, String color) {
        // Checks if a database entry has been made
        makeCheck(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReport SET Color=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, color);
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setUrge(long userId, int urge) {
        // Checks if a database entry has been made
        makeCheck(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReport SET Urge=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(urge));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void newEodReport(long userId) {
        LOGGER.info("Made a new EOD Report database entry with the user id of " + userId + ".");
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO EODReport" +
                        "(UserId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void newEodReportDiscord(long userId) {
        LOGGER.info("Made a new EOD Report Discord database entry with the user id of " + userId + ".");
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO EODReportDiscordBot" +
                        "(UserId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void newQuestion(long userId) {
        LOGGER.info("Made a new Question database entry with the user id of " + userId + ".");
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Questions" +
                        "(UserId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static synchronized boolean hasReportCreated(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT LastAnsweredTime FROM EODReport WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static synchronized void makeCheck(long userId) {
        boolean hasReportCreated = hasReportCreated(userId);
        if (!hasReportCreated) {
            newEodReport(userId);
        }
    }

    private static synchronized void makeCheckDiscord(long userId) {
        boolean hasReportCreated = hasReportCreatedDiscord(userId);
        if (!hasReportCreated) {
            newEodReportDiscord(userId);
        }
    }

    private static synchronized boolean hasReportCreatedDiscord(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT ReportStreak FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static synchronized boolean hasQuestionCreated(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Question FROM Questions WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // For EODReportDiscordBot database table
    public static synchronized void setReportStreak(long userId, int streak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET ReportStreak=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(streak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void incrementReportStreak(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int reportStreak = getReportStreak(userId);
        setReportStreak(userId, reportStreak + 1);
    }

    public static synchronized int getReportStreak(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT ReportStreak FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ReportStreak");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setRelapseFreeStreak(long userId, int streak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET RelapseFreeStreak=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(streak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getTotalUrge(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT TotalUrge FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalUrge");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setTotalUrge(long userId, int streak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET TotalUrge=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(streak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void incrementTotalUrge(long userId, int urge) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int totalUrge = getTotalUrge(userId);
        setTotalUrge(userId, totalUrge + urge);
    }

    // here

    public static synchronized int getColorGreenCount(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT GreenCount FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("GreenCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setColorGreenCount(long userId, int streak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET GreenCount=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(streak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void incrementColorGreenCount(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int totalUrge = getColorGreenCount(userId) + 1;
        setColorGreenCount(userId, totalUrge);
    }

    // yellow

    public static synchronized int getColorYellowCount(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT YellowCount FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("YellowCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setColorYellowCount(long userId, int streak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET YellowCount=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(streak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void incrementColorYellowCount(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int totalUrge = getColorYellowCount(userId) + 1;
        setColorYellowCount(userId, totalUrge);
    }

    // green

    public static synchronized int getColorRedCount(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT RedCount FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RedCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setColorRedCount(long userId, int streak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET RedCount=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(streak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void incrementColorRedCount(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int totalUrge = getColorRedCount(userId) + 1;
        setColorRedCount(userId, totalUrge);
    }

    // end

    public static synchronized void incrementTotalAnswered(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int totalAnswered = getTotalAnswered(userId);
        setTotalAnswered(userId, totalAnswered + 1);
    }

    public static synchronized int getTotalAnswered(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT TotalAnswered FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalAnswered");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setTotalAnswered(long userId, int streak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET TotalAnswered=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(streak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void incrementRelapseFreeStreak(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int relapseFreeStreak = getRelapseFreeStreak(userId);
        setRelapseFreeStreak(userId, relapseFreeStreak + 1);
    }

    public static synchronized int getRelapseFreeStreak(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT RelapseFreeStreak FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RelapseFreeStreak");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setMissedDays(long userId, int missedDays) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET MissedDays=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(missedDays));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void incrementMissedDays(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int missedDays = getMissedDays(userId);
        setMissedDays(userId, missedDays + 1);
    }

    public static synchronized int getMissedDays(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT MissedDays FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("MissedDays");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setRelapseCount(long userId, int missedDays) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET RelapseCount=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(missedDays));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void incrementRelapseCount(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        int missedDays = getRelapseCount(userId);
        setRelapseCount(userId, missedDays + 1);
    }

    public static synchronized int getRelapseCount(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT RelapseCount FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RelapseCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized int getIsEODAnswered(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT IsEODAnswered FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("IsEODAnswered");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setIsEODAnswered(long userId, boolean isEODAnswered) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        int input = isEODAnswered ? 1 : 0;

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET IsEODAnswered=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(input));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setIsEODAnswered(long userId, int isEODAnswered) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET IsEODAnswered=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(isEODAnswered));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getBestRelapseFreeStreak(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT BestRelapseFreeStreak FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("BestRelapseFreeStreak");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setBestRelapseFreeStreak(long userId, int bestRelapseFreeStreak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET BestRelapseFreeStreak=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(bestRelapseFreeStreak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getBestReportStreak(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT BestReportStreak FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("BestReportStreak");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setBestReportStreak(long userId, int bestReportStreak) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET BestReportStreak=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(bestReportStreak));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getExp(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT EXPVC FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("EXPVC");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setAccEXP(long userId, int exp) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET AccEXP=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(exp));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getAccExp(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT AccEXP FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("AccEXP");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setExp(long userId, int exp) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET EXPVC=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(exp));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addExpBy(long userId, int exp) {
        int oldExp = getExp(userId);
        setExp(userId, oldExp + exp);
    }

    public static synchronized void addExpAccBy(long userId, int exp) {
        int oldExp = getAccExp(userId);
        setAccEXP(userId, oldExp + exp);
    }

    public static synchronized int getExpText(long userId) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT EXPTEXT FROM EODReportDiscordBot WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("EXPTEXT");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized void setExpText(long userId, int exp) {
        // Checks if a database entry has been made
        makeCheckDiscord(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE EODReportDiscordBot SET EXPTEXT=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(exp));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addExpByText(long userId, int exp) {
        int oldExp = getExpText(userId);
        setExpText(userId, oldExp + exp);
    }

    // For Questions database table
    private static final String SPLIT_CHAR = "%&%&";
    private static synchronized void makeCheckQuestion(long userId) {
        boolean hasReportCreated = hasQuestionCreated(userId);
        if (!hasReportCreated) {
            newQuestion(userId);
        }
    }

    public static synchronized String getQuestions(long userId) {
        // Checks if a database entry has been made
        makeCheckQuestion(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Question FROM Questions WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String question = resultSet.getString("Question");
                    if (question.equals("None")) return null;
                    return question;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static synchronized String getRecordId(long userId) {
        // Checks if a database entry has been made
        makeCheckQuestion(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT RecordId FROM AirtableUser WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("RecordId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static synchronized String getOverviewId(long userId) {
        // Checks if a database entry has been made
        makeCheckQuestion(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT OverviewId FROM AirtableUser WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("OverviewId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static synchronized List<String> getQuestionsList(long userId) {
        List<String> questions = new ArrayList<>();
        // Checks if a database entry has been made
        makeCheckQuestion(userId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Question FROM Questions WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String question = resultSet.getString("Question");
                    if (question == null || question.equals("None")) return questions;
                    String[] split = question.split(SPLIT_CHAR);
                    questions.addAll(Arrays.asList(split));
                    questions.remove("None");
                    questions.remove("null");
                    return questions;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public static synchronized void setQuestions(long userId, String questions) {
        // Checks if a database entry has been made
        makeCheckQuestion(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE Questions SET Question=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(questions));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setOverviewId(long userId, String overviewId) {
        // Checks if a database entry has been made
        makeCheckQuestion(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE AirtableUser SET OverviewId=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(overviewId));
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addQuestions(long userId, String question) {
        // Checks if a database entry has been made
        makeCheckQuestion(userId);
        String questions = getQuestions(userId);
        questions = questions + SPLIT_CHAR + question;

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE Questions SET Question=? WHERE UserId=?"
                )) {

            preparedStatement.setString(1, questions);
            preparedStatement.setString(2, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void deleteNewUser(long userId) {

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("DELETE FROM NewUser WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void deleteSecondUser(long userId) {

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("DELETE FROM SecondUser WHERE UserId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addSelectRoleCount(long roleId) {
        // Checks if a database entry has been made
        checkRole(roleId);
        int count = getCount(roleId) + 1;

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE SelectRoles SET Count=? WHERE RoleId=?"
                )) {

            preparedStatement.setString(1, String.valueOf(count));
            preparedStatement.setString(2, String.valueOf(roleId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void newChallengeAcceptor(long messagId, long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Challenge" + messagId +
                        "(UserId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info("Challenge doesn't exists");
        }
    }

    public static synchronized void newRecordId(long userId, String recordId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO AirtableUser" +
                        "(UserId, RecordId)" +
                        "VALUES (?, ?);")) {

            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, String.valueOf(recordId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static synchronized void newSelectRole(long roleId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO SelectRoles" +
                        "(RoleId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(roleId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static synchronized void newUserJoined(long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO NewUser" +
                        "(UserId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static synchronized void secodnUserJoined(long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO SecondUser" +
                        "(UserId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static synchronized int getCount(long roleId) {
        checkRole(roleId);
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Count FROM SelectRoles WHERE RoleId = ?")) {

            preparedStatement.setString(1, String.valueOf(roleId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized boolean hasRoleExist(long roleId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Count FROM SelectRoles WHERE RoleId = ?")) {

            preparedStatement.setString(1, String.valueOf(roleId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized boolean isNewUser(long roleId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT IsNew FROM NewUser WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(roleId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized boolean isSecondUser(long roleId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT IsNew FROM SecondUser WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(roleId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized void checkRole(long roleId) {
        if (!hasRoleExist(roleId)) {
            newSelectRole(roleId);
        }
    }

    public static synchronized String getQuizQuestionContent(long channelId, String info, int questionNumber) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT " + info + " FROM Quiz" + channelId + " WHERE QuestionNumber = ?")) {

            preparedStatement.setString(1, String.valueOf(questionNumber));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(info);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}