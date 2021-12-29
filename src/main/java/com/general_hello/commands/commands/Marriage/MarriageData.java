package com.general_hello.commands.commands.Marriage;

import com.general_hello.commands.Bot;
import com.general_hello.commands.Database.SQLiteDataSource;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("SqlResolve")
public class MarriageData {
    public static int decreaseBy = 4;
    public static void newInfo(long userId, long wifeId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO MarriageData" +
                        "(MainUser, Wife, Son, XP, HouseXP, Happiness, Love, House)" +
                        "VALUES (?, ?, -1, 10, 10, 100, 100, 100);")) {

            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, String.valueOf(wifeId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long getWife(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Wife FROM MarriageData WHERE MainUser = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("Wife");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long getSon(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Son FROM MarriageData WHERE MainUser = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("Son");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long getXP(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT XP FROM MarriageData WHERE MainUser = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("XP");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void addXP(long userId, int xp) {
        xp = (int) (getXP(userId) + xp);
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE MarriageData SET XP=? WHERE MainUser=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(xp));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long getHappiness(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Happiness FROM MarriageData WHERE MainUser = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("Happiness");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void addHappiness(long userId, int happiness) {
        happiness = (int) (getHappiness(userId) + happiness);
        if (happiness > 100) {
            happiness = 100;
        }
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE MarriageData SET Happiness=? WHERE MainUser=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(happiness));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long getLove(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Love FROM MarriageData WHERE MainUser = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("Love");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void addLove(long userId, int love) {
        love = (int) (getLove(userId) + love);
        if (love > 100) {
            love = 100;
        }
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE MarriageData SET Love=? WHERE MainUser=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(love));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long getHouseStatus(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT House FROM MarriageData WHERE MainUser = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("House");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void addHouseStatus(long userId, int house) {
        house = (int) (getHouseStatus(userId) + house);

        if (house > 100) {
            house = 100;
        }
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE MarriageData SET House=? WHERE MainUser=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(house));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long getHouseXP(long userId) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT HouseXP FROM MarriageData WHERE MainUser = ?")) {

            preparedStatement.setString(1, String.valueOf(userId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("HouseXP");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void addHouseXP(long userId, int houseXP) {
        houseXP = (int) (getHouseXP(userId) + houseXP);
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE MarriageData SET HouseXP=? WHERE MainUser=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(houseXP));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void adoptSon(long userId, long sonId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE MarriageData SET Son=? WHERE MainUser=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(sonId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void leaveSon(long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE MarriageData SET Son=-1 WHERE MainUser=?"
                )) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void divorce(long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("DELETE FROM MarriageData WHERE MainUser=?"
                )) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void runDeduct() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(MarriageData::deduct, 1, 1, TimeUnit.HOURS);
    }

    private static void deduct() {
        List<User> users = Bot.jda.getUsers();
        int x = 0;
        while (x < users.size()) {
            User user = users.get(x);
            if (MarriageData.getWife(user.getIdLong()) != -1) {
                long idLong = user.getIdLong();
                MarriageData.addHappiness(idLong, -decreaseBy);
                MarriageData.addHouseStatus(idLong, -decreaseBy);
                MarriageData.addLove(idLong, -decreaseBy);
                if (MarriageData.getHappiness(idLong) < 1 || MarriageData.getLove(idLong) < 1 || MarriageData.getHouseStatus(idLong) < 1) {
                    MarriageData.divorce(idLong);
                }
            }
            x++;
        }
    }
}
