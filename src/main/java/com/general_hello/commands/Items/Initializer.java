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
        // Format: new Skills("NAME", COST, "EMOJI", "DESCRIPTION", Rank.[RANK], [Patreon skill or not (true/false)], ENERGY_TO_USE_SKILL, [Neo Devil fruit skill or not (true/false)], [Premium skill or not (true/false)], EffectTypes.EFECTNAME,.... (Can add up to however you want));
        new Skill("The Summoner", 1_000, "<:blackhole:939699625600888852>", "Summons a powerful attack and heals yourself by 100HP", Rank.R1, false, 10, true, false, EffectTypes.COPY_SKILLS, EffectTypes.DESTRUCTIBLE_BARRIER);
        new Skill("The Summoner More Skills", 1_000, "<:blackhole:939699625600888852>", "Summons a powerful attack and heals yourself by 100HP", Rank.R1, false, 10, true, false, EffectTypes.DAMAGE, EffectTypes.COMPOUND_RESIDUAL, EffectTypes.DROWN);
        new Skill("The Summoner Premium", 1_000, "<:blackhole:939699625600888852>", "Summons a powerful attack and heals yourself by 100HP", Rank.R1, true, 10, false, true, EffectTypes.COPY_SKILLS, EffectTypes.INDESTRUCTIBLE_BARRIER);
        // Limited items
        // Format: new LimitedItems("NAME", COST, "EMOJI", "DESCRIPTION", Rank.[RANK], [Patreon item or not (true/false)], MAX_STOCK, CURRENT_STOCK, [Premium item or not (true/false)]);
        new LimitedItems("The Healing Card", 8_000, "<a:king_card:939699732513701910>", "Heals you", Rank.R1, false, 1000, 999, true);
        // Items
        // Format: new Item("NAME", COST, "EMOJI", "DESCRIPTION", Rank.[RANK], [Premium item or not (true/false)], [Patreon item or not (true/false)]);
        new Item("The Plane of Health Premium", 10_000, "<a:blue_airplane:939704592067878952>", "Heals you by 100HP", Rank.R1, true, true);
        new Item("The Plane of Health", 10_000, "<a:blue_airplane:939704592067878952>", "Heals you by 100HP", Rank.R1, false, true);
        new Item("The Item", 10_000, "<a:blue_airplane:939704592067878952>", "Heals you by 100HP", Rank.R1, false, false);
        // Limited items
        // Format: new LimitedItems("NAME", COST, "EMOJI", "DESCRIPTION", Rank.[RANK], [Patreon item or not (true/false)], MAX_STOCK, CURRENT_STOCK, [Premium item or not (true/false)]);
        new LimitedItems("The Card", 8_000, "<a:king_card:939699732513701910>", "Heals you", Rank.R1, false, 1000, 999, false);
        // You need to call the achievement to add it to /achievements
        Achievement run = Achievement.NEW_ACCOUNT;
        run = Achievement.REACH_R2;
        run = Achievement.REACH_R3;
        run = Achievement.REACH_R4;
        run = Achievement.REACH_R5;
        run = Achievement.REACH_R6;
        run = Achievement.REACH_R7;
        run = Achievement.REACH_R8;
    }

    // Sends a request to the SQLite database and makes a new "info" to it
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
