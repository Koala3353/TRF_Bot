package com.general_hello.commands.Objects.User;

import com.general_hello.Bot;
import com.general_hello.commands.Database.SQLiteDataSource;
import com.general_hello.commands.Objects.RPGEmojis;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Player {
    private final int userId;
    private int hp;
    private int melee;
    private int magic;
    private int neoDevilFruit;
    private String profession;
    private int professionExp;
    private int strength;
    private int endurance;
    private int intelligence;
    private int willpower;
    private int speed;
    private Rank rank;
    private int exp;
    private int level;
    private int aiDefeated;
    private int pvpFought;
    private int pvpWon;
    private int pvpLost;
    private int rankWins;
    private int rankLoss;
    private int rankFought;
    private String bounty;
    private String achievementTitle;
    private int likes;
    private long marriagePartnerId;
    private long senseiId;
    private int berri;
    private int rainbowShards;
    private int skillSlotsCap;

    public Player(int userId) {
        this.userId = userId;
        this.hp = gotMynt("hp");
        this.melee = gotMynt("melee");
        this.magic = gotMynt("magic");
        this.neoDevilFruit = gotMynt("neoDevilFruit");
        this.profession = getSavedThingFromDatabase("profession");
        this.professionExp = gotMynt("professionExp");
        this.strength = gotMynt("strength");
        this.endurance = gotMynt("endurance");
        this.intelligence = gotMynt("intelligence");
        this.willpower = gotMynt("willpower");
        this.speed = gotMynt("speed");
        this.rank = getRankFromInt(gotMynt("rank"));
        this.exp = gotMynt("exp");
        this.level = gotMynt("level");
        this.aiDefeated = gotMynt("aiDefeated");
        this.pvpWon = gotMynt("pvpWon");
        this.pvpLost = gotMynt("pvpLost");
        this.pvpFought = this.pvpWon + this.pvpLost;
        this.rankWins = gotMynt("rankWins");
        this.rankLoss = gotMynt("rankLoss");
        this.rankFought = this.rankWins + this.rankLoss;
        this.bounty = getSavedThingFromDatabase("bounty");
        this.achievementTitle = getSavedThingFromDatabase("achievementTitle");
        this.likes = gotMynt("likes");
        this.marriagePartnerId = getSavedThingFromDatabaseLong("marriagePartnerId");
        this.senseiId = getSavedThingFromDatabaseLong("senseiId");
        this.berri = gotMynt("berri");
        this.rainbowShards = gotMynt("rainbowShards");
        this.skillSlotsCap = gotMynt("skillSlotsCap");
    }

    private int gotMynt(String thing) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT " + thing + " FROM Player WHERE userId = " + userId)) {

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(thing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private long getSavedThingFromDatabaseLong(String thing) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT " + thing + " FROM Player WHERE userId = " + userId)) {

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(thing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private String getSavedThingFromDatabase(String thing) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT " + thing + " FROM Player WHERE userId = " + userId)) {

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(thing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getHp() {
        return hp;
    }

    public int getMelee() {
        return melee;
    }

    public Rank getRankFromInt(int rank) {
        return switch (rank) {
            case 1 -> Rank.R1;
            case 2 -> Rank.R2;
            case 3 -> Rank.R3;
            case 4 -> Rank.R4;
            case 5 -> Rank.R5;
            case 6 -> Rank.R6;
            case 7 -> Rank.R7;
            case 8 -> Rank.R8;
            default -> Rank.R1;
        };
    }

    public int getUserId() {
        return userId;
    }

    public int getProfessionExp() {
        return professionExp;
    }

    public int getMagic() {
        return magic;
    }

    public int getNeoDevilFruit() {
        return neoDevilFruit;
    }

    public String getProfession() {
        return profession;
    }

    public int getStrength() {
        return strength;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getWillpower() {
        return willpower;
    }

    public int getSpeed() {
        return speed;
    }

    public Rank getRank() {
        return rank;
    }

    public int getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public int getAiDefeated() {
        return aiDefeated;
    }

    public int getPvpFought() {
        return pvpFought;
    }

    public int getBerri() {
        return berri;
    }

    public int getRainbowShards() {
        return rainbowShards;
    }

    public int getSkillSlotsCap() {
        return skillSlotsCap;
    }

    public int getPvpWon() {
        return pvpWon;
    }

    public int getPvpLost() {
        return pvpLost;
    }

    public int getRankWins() {
        return rankWins;
    }

    public int getRankLoss() {
        return rankLoss;
    }

    public int getRankFought() {
        return rankFought;
    }

    public int getBounty() {
        String[] split = this.bounty.split("/");
        int bounty = Integer.parseInt(split[1]);
        return (bounty == -1 ? 0 : bounty);
    }

    private String getEmojiFromNumber(int number) {
        if (number == 1) {
            return RPGEmojis.berri;
        }

        return RPGEmojis.rainbowShards;
    }

    public String getBountyFancyString() {
        String[] split = this.bounty.split("/");
        int bounty = Integer.parseInt(split[1]);
        String emoji = getEmojiFromNumber(Integer.parseInt(split[0]));
        return (bounty == -1 ? "No bounty" : (emoji + " " + bounty));
    }

    public String getAchievementTitle() {
        return achievementTitle;
    }

    public int getLikes() {
        return likes;
    }

    public long getMarriagePartnerId() {
        return marriagePartnerId;
    }

    public User getMarriagePartnerUser() {
        return Bot.jda.getUserById(marriagePartnerId);
    }

    public long getSenseiId() {
        return senseiId;
    }

    public User getSenseiUser() {
        return Bot.jda.getUserById(senseiId);
    }

    // Setters

    public static void setInDatabase(long userId, int toChange, String setting) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE Settings SET " + setting + "=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(toChange));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setInDatabase(long userId, String toChange, String setting) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE Settings SET " + setting + "=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(toChange));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setInDatabase(long userId, long toChange, String setting) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("UPDATE Settings SET " + setting + "=? WHERE UserId=?"
                )) {

            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.setString(1, String.valueOf(toChange));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Player setHp(int hp) {
        this.hp = hp;
        setInDatabase(this.userId, hp, "hp");
        return this;
    }

    public Player setMelee(int melee) {
        this.melee = melee;
        setInDatabase(this.userId, melee, "melee");
        return this;
    }

    public Player setMagic(int magic) {
        this.magic = magic;
        setInDatabase(this.userId, magic, "magic");
        return this;
    }

    public Player setNeoDevilFruit(int neoDevilFruit) {
        this.neoDevilFruit = neoDevilFruit;
        setInDatabase(this.userId, neoDevilFruit, "neoDevilFruit");
        return this;
    }

    public Player setProfession(String profession) {
        this.profession = profession;
        setInDatabase(this.userId, profession, "");
        return this;
    }

    public Player setProfessionExp(int professionExp) {
        this.professionExp = professionExp;
        setInDatabase(this.userId, professionExp, "professionExp");
        return this;
    }

    public Player setStrength(int strength) {
        this.strength = strength;
        setInDatabase(this.userId, strength, "strength");
        return this;
    }

    public Player setEndurance(int endurance) {
        this.endurance = endurance;
        setInDatabase(this.userId, endurance, "endurance");
        return this;
    }

    public Player setIntelligence(int intelligence) {
        this.intelligence = intelligence;
        setInDatabase(this.userId, intelligence, "intelligence");
        return this;
    }

    public Player setWillpower(int willpower) {
        this.willpower = willpower;
        setInDatabase(this.userId, willpower, "willpower");
        return this;
    }

    public Player setSpeed(int speed) {
        this.speed = speed;
        setInDatabase(this.userId, speed, "speed");
        return this;
    }

    public int getIntFromRank(Rank rank) {
        if (rank.equals(Rank.R1)) {
            return 1;
        } else if (rank.equals(Rank.R2)) {
            return 2;
        } else if (rank.equals(Rank.R3)) {
            return 3;
        } else if (rank.equals(Rank.R4)) {
            return 4;
        } else if (rank.equals(Rank.R5)) {
            return 5;
        } else if (rank.equals(Rank.R6)) {
            return 6;
        } else if (rank.equals(Rank.R7)) {
            return 7;
        } else if (rank.equals(Rank.R8)) {
            return 8;
        }

        return 1;
    }

    public Player setRank(Rank rank) {
        this.rank = rank;
        setInDatabase(this.userId, getIntFromRank(rank), "rank");
        return this;
    }

    public Player setExp(int exp) {
        this.exp = exp;
        setInDatabase(this.userId, exp, "exp");
        return this;
    }

    public Player setLevel(int level) {
        this.level = level;
        setInDatabase(this.userId, level, "level");
        return this;
    }

    public Player setAiDefeated(int aiDefeated) {
        this.aiDefeated = aiDefeated;
        setInDatabase(this.userId, aiDefeated, "aiDefeated");
        return this;
    }

    public Player setPvpWon(int pvpWon) {
        this.pvpWon = pvpWon;
        this.pvpFought++;
        setInDatabase(this.userId, pvpWon, "pvpWon");
        return this;
    }

    public Player setPvpLost(int pvpLost) {
        this.pvpLost = pvpLost;
        this.pvpFought++;
        setInDatabase(this.userId, pvpLost, "pvpLost");
        return this;
    }

    public Player setRankWins(int rankWins) {
        this.rankWins = rankWins;
        this.rankFought++;
        setInDatabase(this.userId, rankWins, "rankWins");
        return this;
    }

    public Player setRankLoss(int rankLoss) {
        this.rankLoss = rankLoss;
        this.rankFought++;
        setInDatabase(this.userId, rankLoss, "rankLoss");
        return this;
    }

    public Player setBounty(String bounty) {
        this.bounty = bounty;
        setInDatabase(this.userId, bounty, "bounty");
        return this;
    }

    public Player setAchievementTitle(String achievementTitle) {
        this.achievementTitle = achievementTitle;
        setInDatabase(this.userId, achievementTitle, "achievementTitle");
        return this;
    }

    public Player setLikes(int likes) {
        this.likes = likes;
        setInDatabase(this.userId, likes, "likes");
        return this;
    }

    public Player setMarriagePartnerId(long marriagePartnerId) {
        this.marriagePartnerId = marriagePartnerId;
        setInDatabase(this.userId, marriagePartnerId, "marriagePartnerId");
        return this;
    }

    public Player setSenseiId(long senseiId) {
        this.senseiId = senseiId;
        setInDatabase(this.userId, senseiId, "senseiId");
        return this;
    }

    public Player setBerri(int berri) {
        this.berri = berri;
        setInDatabase(this.userId, berri, "berri");
        return this;
    }

    public Player setRainbowShards(int rainbowShards) {
        this.rainbowShards = rainbowShards;
        setInDatabase(this.userId, rainbowShards, "rainbowShards");
        return this;
    }

    public Player setSkillSlotsCap(int skillSlotsCap) {
        this.skillSlotsCap = skillSlotsCap;
        setInDatabase(this.userId, skillSlotsCap, "skillSlotsCap");
        return this;
    }
}
