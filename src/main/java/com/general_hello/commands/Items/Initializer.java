package com.general_hello.commands.Items;

import com.general_hello.commands.Database.SQLiteDataSource;
import com.general_hello.commands.Objects.*;
import com.general_hello.commands.Objects.Object;
import com.general_hello.commands.Objects.User.Rank;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Initializer {
    public static HashMap<String, Skill> nameToSkill = new HashMap<>();
    public static HashMap<String, Item> nameToItem = new HashMap<>();
    public static HashMap<String, Object> allObjects = new HashMap<>();
    public static HashMap<String, Achievement> allAchievements = new HashMap<>();
    public static ArrayList<String> allNames = new ArrayList<>();
    public static ArrayList<String> allAchievementNames = new ArrayList<>();
    public static ArrayList<Achievement> achievements = new ArrayList<>();
    public static ArrayList<Skill> skills = new ArrayList<>();
    public static ArrayList<Item> items = new ArrayList<>();

    public static void initializer() {
        // A basic skill for testing
        new Skill("The Summoner", 1_000, "<:blackhole:939699625600888852>", "Summons a powerful attack and heals yourself by 100HP", Rank.R1, true, 10, true, EffectTypes.COPY_SKILLS, EffectTypes.BARRIER);
        Achievement run = Achievement.NEW_ACCOUNT;
    }

    public static void newUser(long userId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Player" +
                        "(userId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Skills" +
                        "(userId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("INSERT INTO Achievement" +
                        "(userId)" +
                        "VALUES (?);")) {

            preparedStatement.setString(1, String.valueOf(userId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
