package com.general_hello.commands.objects;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.Arrays;
import java.util.List;

public enum SportType {
    AMERICAN_FOOTBALL("American Football", 990836205035851826L, "NCAAF", "NFL"),
    BASEBALL("Baseball", 990836225466310687L, "MLB", "NBA"),
    CRICKET("Cricket", 990836244214853662L, "ICC World Cup", "IPL", "One Day Internationals", "Test Match"),
    GOLF("Golf", 990836255690489856L, "Masters Tournament", "PGA Championship", "The Open", "US Open"),
    ICE_HOCKEY("Ice Hockey", 990836272815800340L, "NHL"),
    MIXED_MARTIAL_ARTS("Mixed Martial Arts", 990836306131173386L, "MMA"),
    SOCCER("Soccer", 990836322686079006L, "EPL", "FA Cup", "Fifa World Cup", "Ligue 1 - France",
            "Bundesliga - Germany", "Serie A - Italy", "Dutch Eresdivisie - Netherlands",
            "La Liga - Spain", "UEFA Champions League"),
    TENNIS("Tennis", 990836334212034600L, "ATP French Open", "ATP US Open", "ATP Wimbledon");

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
