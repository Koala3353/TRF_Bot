package com.general_hello.commands.Items;

import com.general_hello.commands.Database.SQLiteDataSource;
import com.general_hello.commands.Objects.Item;
import com.general_hello.commands.Objects.Object;
import com.general_hello.commands.Objects.Skill;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Initializer {
    public static HashMap<String, Skill> nameToSkill = new HashMap<>();
    public static HashMap<String, Item> nameToItem = new HashMap<>();
    public static HashMap<String, Object> allObjects = new HashMap<>();
    public static ArrayList<String> allNames = new ArrayList<>();
    public static ArrayList<Skill> skills = new ArrayList<>();
    public static ArrayList<Item> items = new ArrayList<>();

    public static void initializer() {
        // A basic skill for testing
        new Skill("The Summoner", 1_000, "<:blackhole:939699625600888852>", "Summons a powerful attack and heals yourself by 100HP", 1_000, 100);
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
    }
}
