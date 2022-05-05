package com.general_hello.commands.Objects;

import com.general_hello.Bot;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.Level.Level;
import net.dv8tion.jda.api.entities.User;

import java.text.DecimalFormat;

public class Player {
    private static final DecimalFormat formatter = new DecimalFormat("#,###");
    private final long userid;
    private int points;
    private int gamesFoughtToday;
    private final Level level;

    public Player(long userid, String roleName) {
        if (!DataUtils.hasAccount(userid, Level.valueOf(roleName))) {
            DataUtils.newAccount(userid, Level.valueOf(roleName));
        }
        this.userid = userid;
        this.points = DataUtils.getPoints(userid, Level.valueOf(roleName));
        this.gamesFoughtToday = DataUtils.getGamesFoughtToday(userid, Level.valueOf(roleName));
        this.level = Level.valueOf(roleName);
    }

    public Player(long userid, Level level) {
        if (!DataUtils.hasAccount(userid, level)) {
            DataUtils.newAccount(userid, level);
        }
        this.userid = userid;
        this.points = DataUtils.getPoints(userid, level);
        this.gamesFoughtToday = DataUtils.getGamesFoughtToday(userid, level);
        this.level = level;
    }

    public long getUserid() {
        return userid;
    }

    public User getUser() {
        return Bot.jda.getUserById(userid);
    }

    public int getPoints() {
        return points;
    }

    public Level getLevel() {
        return level;
    }

    public String getFormattedPoints() {
        return formatter.format(points);
    }

    public int getGamesFoughtToday() {
        return gamesFoughtToday;
    }

    public Player setPoints(int points) {
        this.points = getPoints() + points;
        DataUtils.addPoints(this.userid, points, level);
        return this;
    }

    public Player addGamesFoughtToday() {
        this.gamesFoughtToday++;
        DataUtils.addGamesFoughtToday(this.userid, level);
        return this;
    }

    public Player resetGamesFoughtToday() {
        this.gamesFoughtToday = 0;
        DataUtils.resetGamesFoughtToday(this.userid, level);
        return this;
    }
}
