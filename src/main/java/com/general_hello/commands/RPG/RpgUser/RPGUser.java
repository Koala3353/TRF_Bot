package com.general_hello.commands.RPG.RpgUser;

import com.general_hello.commands.Database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RPGUser {
    public static int getItemCount(long userId, String item) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT " + SQLiteDataSource.filter(item) + " FROM RPGData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(SQLiteDataSource.filter(item));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getShekels(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Shekels FROM RPGData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Shekels");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getLevelPoint(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Level FROM RPGData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Level");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void newInfo(long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO RPGData" +
                        "(UserId, Shekels, Level)" +
                        "VALUES (?, 100, 0);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
