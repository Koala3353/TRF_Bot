package com.general_hello.commands.Objects.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public enum Rank {
    RABBITGAROO("Rabbitgaroo", "Bottom Rank", 0, 999),
    SOLDIER("Soldier", "Entry Rank", 1000, 2999),
    TROLL("Troll", "Mid Level Rank", 3000, 4999),
    CRUCIBLE_KNIGHT("Crucible Knight", "High Level Rank", 5000, 9999),
    ABDUCTOR_VIRGIN("Abductor Virgin", "Elite Rank", 10000, 14999),
    RUNEBEAR("Runebear", "Legendary Rank", 15000, 1_000_000);

    // Variables of the object
    private final String name;
    private final String description;
    private final int minimumScore;
    private final int maximumScore;
    public static HashMap<String, Rank> nameToRank = new HashMap<>();

    // Initializer
    Rank(String name, String description, int minimumScore, int maximumScore) {
        this.name = name;
        this.description = description;
        this.minimumScore = minimumScore;
        this.maximumScore = maximumScore;
    }

    public String getName() {
        return name;
    }

    public String getFilteredName() {
        return name.replaceAll("\\s+", "");
    }

    public String getDescription() {
        return description;
    }

    public int getMinimumScore() {
        return minimumScore;
    }

    public int getMaximumScore() {
        return maximumScore;
    }

    public static Rank getRankFromPoints(int points) {
        for (Rank rank : Rank.values()) {
            if (points >= rank.getMinimumScore() && points <= rank.getMaximumScore()) {
                return rank;
            }
        }
        return Rank.RABBITGAROO;
    }

    public static int getPointsWin(Rank winner, Rank loser) {
        boolean firstRankHigherThanSecond = isFirstRankHigherOrEqualThanSecond(winner, loser);
        int points;
        if (firstRankHigherThanSecond) {
            int stagesLower = howManyStagesLower(loser, winner);
            double multi = 100 * Math.pow(1.45, stagesLower);
            points = (int) multi;
        } else {
            int stagesLower = howManyStagesHigher(loser, winner);
            double multi = 100 * Math.pow(0.55, stagesLower);
            points = (int) multi;
        }
        return points;
    }

    public static int getPointsLost(Rank winner, Rank loser) {
        boolean firstRankHigherThanSecond = isFirstRankHigherOrEqualThanSecond(winner, loser);
        int points;
        if (firstRankHigherThanSecond) {
            int stagesLower = howManyStagesHigher(winner, loser);
            double multi = 60 * Math.pow(0.55, (stagesLower));
            points = (int) multi;
        } else {
            int stagesLower = howManyStagesLower(winner, loser);
            double multi = 60 * Math.pow(1.45, (stagesLower));
            points = (int) multi;
        }
        return -points;
    }

    public static boolean isFirstRankHigherOrEqualThanSecond(Rank first, Rank second) {
        HashMap<Rank, List<Rank>> hashMap = new HashMap<>();
        hashMap.put(RABBITGAROO, new ArrayList<>());
        hashMap.put(SOLDIER, List.of(RABBITGAROO));
        hashMap.put(TROLL, List.of(SOLDIER, RABBITGAROO));
        hashMap.put(CRUCIBLE_KNIGHT, List.of(SOLDIER, RABBITGAROO, TROLL));
        hashMap.put(ABDUCTOR_VIRGIN, List.of(CRUCIBLE_KNIGHT, SOLDIER, RABBITGAROO, TROLL));
        hashMap.put(RUNEBEAR, List.of(ABDUCTOR_VIRGIN, CRUCIBLE_KNIGHT, SOLDIER, RABBITGAROO, TROLL));
        return !hashMap.get(first).contains(second);
    }

    public static int howManyStagesLower(Rank first, Rank second) {
        ArrayList<Rank> list = new ArrayList<>(List.of(RABBITGAROO, SOLDIER, TROLL, CRUCIBLE_KNIGHT, ABDUCTOR_VIRGIN, RUNEBEAR));
        int x = 0;
        boolean startCounting = false;
        Collections.reverse(list);
        for (Rank rank : list) {
            if (rank.equals(first)) {
                startCounting = true;
            }
            if (startCounting) {
                x++;
            }

            if (rank.equals(second)) {
                break;
            }
        }
        return x-1;
    }

    public static int howManyStagesHigher(Rank first, Rank second) {
        ArrayList<Rank> list = new ArrayList<>(List.of(RABBITGAROO, SOLDIER, TROLL, CRUCIBLE_KNIGHT, ABDUCTOR_VIRGIN, RUNEBEAR));
        int x = 0;
        boolean startCounting = false;
        for (Rank rank : list) {
            if (rank.equals(first)) {
                startCounting = true;
            }
            if (startCounting) {
                x++;
            }

            if (rank.equals(second)) {
                break;
            }
        }
        return x-1;
    }
}
