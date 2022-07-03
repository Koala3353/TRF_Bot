package com.general_hello.commands.objects;

import com.general_hello.commands.utils.OddsGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private final SportType sportType;
    private final long gameTime;
    private final String homeTeam;
    private final String awayTeam;
    private final String sport;
    private double homePrice;
    private double awayPrice;
    private final String id;
    private final String sportKey;
    private int editCount;

    public Game(SportType sportType, long gameTime, String homeTeam, String awayTeam, String id, String sportKey, double homePrice, double awayPrice) {
        this.sportType = sportType;
        this.gameTime = gameTime;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homePrice = homePrice;
        this.awayPrice = awayPrice;
        this.id = id;
        this.sport = sportKey;
        this.sportKey = OddsGetter.KEY_OF_SPORT.get(sportKey);
        long timeBeforeTheGame = getGameTime() - Instant.now().getEpochSecond();
        int hoursBeforeTheGame = (int) (timeBeforeTheGame / 3600);
        LOGGER.info("For game " + getId() + " the game will start in " + hoursBeforeTheGame + " hours");
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        long delay = getDelay();
        if (delay != -1) {
            if (hoursBeforeTheGame >= 12) {
                this.editCount = 0;
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), delay, TimeUnit.SECONDS);
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), delay + (3600*9), TimeUnit.SECONDS);
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), delay + (3600*11), TimeUnit.SECONDS);
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), timeBeforeTheGame, TimeUnit.SECONDS);
            } else if (hoursBeforeTheGame >= 3) {
                this.editCount = 1;
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), 120, TimeUnit.SECONDS);
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), timeBeforeTheGame, TimeUnit.SECONDS);
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), delay, TimeUnit.SECONDS);
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), delay + (3600*2), TimeUnit.SECONDS);
            } else if (hoursBeforeTheGame >= 1) {
                this.editCount = 2;
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), 120, TimeUnit.SECONDS);
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), timeBeforeTheGame, TimeUnit.SECONDS);
                scheduler.schedule(new OddsGetter.GetOddsSpecificTask(getSportKey(), id), delay, TimeUnit.SECONDS);
            }
        }
    }

    public Game(SportType sportType, long gameTime, String homeTeam, String awayTeam, String id, String sportKey, double homePrice, double awayPrice, boolean startTask) {
        this.sportType = sportType;
        this.gameTime = gameTime;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homePrice = homePrice;
        this.awayPrice = awayPrice;
        this.id = id;
        this.sport = sportKey;
        this.sportKey = OddsGetter.KEY_OF_SPORT.get(sportKey);
        long timeBeforeTheGame = getGameTime() - Instant.now().getEpochSecond();
        int hoursBeforeTheGame = (int) (timeBeforeTheGame / 3600);
        LOGGER.info("For game " + getId() + " the game will start in " + hoursBeforeTheGame + " hours");
    }


    public double getHomePrice() {
        return homePrice;
    }

    public double getAwayPrice() {
        return awayPrice;
    }

    public Game setHomePrice(double homePrice) {
        this.homePrice = homePrice;
        return this;
    }

    public int getEditCount() {
        return editCount;
    }

    public Game addEditCount() {
        this.editCount++;
        return this;
    }

    public Game setAwayPrice(double awayPrice) {
        this.awayPrice = awayPrice;
        return this;
    }

    private long getDelay() {
        long timeUntilGame = getGameTime() - Instant.now().getEpochSecond();
        long delay = -1;
        if (timeUntilGame >= 3600 * 12) {
            delay = timeUntilGame - 3600 * 12;
        } else if (timeUntilGame >= 3600 * 3) {
            delay = timeUntilGame - 3600 * 3;
        } else if (timeUntilGame >= 3600) {
            delay = timeUntilGame - 3600;
        }

        return delay;
    }

    public String getSportKey() {
        return sportKey;
    }

    public String getSport() {
        return sport;
    }

    public SportType getSportType() {
        return sportType;
    }

    public long getGameTime() {
        return gameTime;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Game ID: " + getId() + " Sport: " + getSport() + " Home: " + getHomeTeam() + " Away: " + getAwayTeam() + " Time: " + getGameTime();
    }
}
