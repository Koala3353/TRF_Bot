package com.general_hello.commands.RPG.RpgUser;

import com.general_hello.commands.Database.SQLiteDataSource;
import com.general_hello.commands.RPG.Items.Initializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RPGUser {
    public static int getItemCount(long userId, String item) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT " + SQLiteDataSource.filter(Initializer.allItems.get(SQLiteDataSource.filter(item)).getName()) + " FROM RPGData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(SQLiteDataSource.filter(Initializer.allItems.get(SQLiteDataSource.filter(item)).getName()));
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

    public static int getBank(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Bank FROM RPGData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Bank");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getBankStorage(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT BankLimit FROM RPGData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("BankLimit");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getHealth(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Health FROM RPGData WHERE UserId = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Health");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void addShekels(long userId, int shekels) {
        int total = (shekels) + getShekels(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE RPGData SET Shekels=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(total));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addBank(long userId, int shekels) {
        int total = (shekels) + getBank(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE RPGData SET Bank=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(total));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addBankStorage(long userId, int shekels) {
        int total = (shekels) + getBankStorage(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE RPGData SET BankLimit=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(total));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void transferShekelsToBank(long userId, int shekels) {
        addShekels(userId, -shekels);
        addBank(userId, shekels);
    }

    public static void transferBankToShekels(long userId, int shekels) {
        addShekels(userId, shekels);
        addBank(userId, -shekels);
    }

    public static void addHealth(long userId, int health) {
        int total = (health) + getHealth(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE RPGData SET Health=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(total));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setHealth(long userId, int health) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE RPGData SET Health=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(health));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addLevelPoints(long userId, int points) {
        int total = (points) + getLevelPoint(userId);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE RPGData SET Level=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(total));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addItem(long userId, int amount, String item) {
        item = SQLiteDataSource.filter(Initializer.allItems.get(SQLiteDataSource.filter(item)).getName());
        int total = (amount) + getItemCount(userId, item);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE RPGData SET " + item + "=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(total));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        "(UserId, Shekels, Bank, BankLimit, Level, Health)" +
                        "VALUES (?, 1000, 100, 1000, 0, 100);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteInfo(long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("DELETE FROM RPGData WHERE UserId IN (?);")
        ) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
