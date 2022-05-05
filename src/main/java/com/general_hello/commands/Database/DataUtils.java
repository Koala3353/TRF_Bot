package com.general_hello.commands.Database;

import com.general_hello.commands.Objects.Level.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataUtils {
    public static boolean hasAccount(long userId, Level level) {
        return getPoints(userId, level) != -1;
    }

    public static int getPoints(long userId, Level level) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT points FROM " + level.getDatabaseName() + " WHERE userid = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("points");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getRange(String type) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT location FROM Range WHERE name = ?")) {

            preparedStatement.setString(1, String.valueOf(type));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("location");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getGamesFoughtToday(long userId, Level level) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT gamesfoughttoday FROM " + level.getDatabaseName() + " WHERE userid = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("gamesfoughttoday");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void newAccount(long userId, Level level) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO " + level.getDatabaseName() +
                        "(userid)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void newRange(String name) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Range(name)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(name));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addGamesFoughtToday(long userId, Level level) {
        int gamesFought = getGamesFoughtToday(userId, level) + 1;
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE " + level.getDatabaseName() + " SET gamesfoughttoday=" + gamesFought + " WHERE userid=" + userId
                )) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetGamesFoughtToday(long userId, Level level) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE " + level.getDatabaseName() + " SET gamesfoughttoday=0 WHERE userid=" + userId
                )) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addPoints(long userId, int points, Level level) {
        int pointsToSet = getPoints(userId, level) + points;
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE " + level.getDatabaseName() + " SET points=" + pointsToSet + " WHERE userid=" + userId
                )) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setRange(String name) {
        int newRange = getRange(name) + 1;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE Range SET location=" + newRange + " WHERE name=" + Level.valueOf(name).name()
                )) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        }
    }
}