package com.general_hello.bot.objects;

import com.general_hello.Config;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.Arrays;
import java.util.List;

public enum SportType {
    AMERICAN_FOOTBALL("American Football", Config.getLong("football"), "NCAAF", "NFL"),
    BASEBALL("Baseball", Config.getLong("baseball"), "MLB", "NBA"),
    CRICKET("Cricket", Config.getLong("cricket"), "ICC World Cup", "IPL", "One Day Internationals", "Test Match"),
    GOLF("Golf", Config.getLong("golf"), "Masters Tournament", "PGA Championship", "The Open", "US Open"),
    ICE_HOCKEY("Ice Hockey", Config.getLong("hockey"), "NHL"),
    MIXED_MARTIAL_ARTS("Mixed Martial Arts", Config.getLong("mixed_arts"), "MMA"),
    SOCCER("Soccer", Config.getLong("soccer"), "EPL", "FA Cup", "Fifa World Cup", "Ligue 1 - France",
            "Bundesliga - Germany", "Serie A - Italy", "Dutch Eresdivisie - Netherlands",
            "La Liga - Spain", "UEFA Champions League"),
    TENNIS("Tennis", Config.getLong("tennis"), "ATP French Open", "ATP US Open", "ATP Wimbledon");

    private final String name;
    private final List<String> sport;
    private final long channelId;

    SportType(String name, long channelId, String... sport) {
        this.name = name;
        this.sport = List.of(sport);
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public List<String> getSport() {
        return sport;
    }

    public long getChannelId() {
        return channelId;
    }

    public static boolean sportContained(SportType sportType, String sport) {
        return sportType.getSport().contains(sport);
    }

    public boolean sportContained(String sport) {
        return sportContained(this, sport);
    }

    public static SportType getObjectFromName(String name) {
        String[] sportTypes = new String[] {
                AMERICAN_FOOTBALL.name(),
                BASEBALL.name(),
                CRICKET.name(),
                GOLF.name(),
                ICE_HOCKEY.name(),
                MIXED_MARTIAL_ARTS.name(),
                SOCCER.name(),
                TENNIS.name()
        };
        return SportType.valueOf(FuzzySearch.extractTop(name, Arrays.asList(sportTypes), 1).get(0).getString());
    }

    public static boolean sportExists(String sport) {
        for (SportType sportType : SportType.values()) {
            if (sportType.getSport().contains(sport)) {
                return true;
            }
        }
        return false;
    }

    public static SportType getSportTypeFromSport(String sport) {
        for (SportType sportType : SportType.values()) {
            if (sportType.getSport().contains(sport)) {
                return sportType;
            }
        }
        return null;
    }
}
