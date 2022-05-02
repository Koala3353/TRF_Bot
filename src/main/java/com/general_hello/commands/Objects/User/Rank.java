package com.general_hello.commands.Objects.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public enum Rank {
    // Rank the has the cap of all the stats within the rank.
    // Full documentation will be on stage 4
    R1(5_000, 10_000, -1, -1, -1, 5_000, 5_000, 5_000, 5_000, 5_000, 100_000, "Newbie", "Newbie", "Newbie", 0, 0, 0),
    R2(10_000, 50_000, 50_000, -1, -1, 10_000, 10_000, 10_000, 10_000, 10_000, 500_000, "Pirates Trainee", "Marines Trainee", "Street Thug", 5, 25, 0),
    R3(50_000, 100_000, 100_000, -1, -1, 50_000, 50_000, 50_000, 50_000, 50_000, 1_000_000, "Junior Pirates", "Junior Marines", "Gangster", 15, 50, 5),
    R4(100_000, 500_000, 500_000, -1, -1, 100_000, 100_000, 100_000, 100_000, 100_000, 10_000_000, "Senior Pirates", "Senior Marines", "Lesser Outlaw", 25, 100, 15),
    R5(500_000, 1_000_000, 1_000_000, 1_000_000, -1, 500_000, 500_000, 500_000, 500_000, 500_000, 50_000_000, "Elite Pirates", "Elite Marines", "Average Outlaw", 40, 150, 50),
    R6(1_000_000, 1_500_000, 1_500_000, 1_500_000, 1_500_000, 1_000_000, 1_000_000, 1_000_000, 1_000_000, 1_000_000, 100_000_000, "Renowned Pirates", "Renowned Marines", "Greater Outlaw", 55, 500, 70),
    R7(1_500_000, 2_000_000, 2_000_000, 2_000_000, 2_000_000, 1_500_000, 1_500_000, 1_500_000, 1_500_000, 1_500_000, 350_000_000, "Veteran Pirates", "Veteran Marines", "Outlaw Boss", 75, 700, 100),
    R8(2_500_000, 3_000_000, 3_000_000, 3_000_000, 3_000_000, 2_500_000, 2_500_000, 2_500_000, 2_500_000, 2_500_000, 750_000_000, "Legendary Pirates", "Legendary Marines", "Legendary Outlaw", 150, 1_000, 150);

    // Variables of the object
    private final int hp;
    private final int meleeCap;
    private final int magicCap;
    private final int neoDevilFruitCap;
    private final int professionCap;
    private final int strength;
    private final int endurance;
    private final int intelligence;
    private final int willpower;
    private final int speed;
    private final int maxExp;
    private final String pirateName;
    private final String marineName;
    private final String outlawName;
    private final int levelReq;
    private final int aiDefeatedReq;
    private final int pvpFoughtReq;

    // Initializer
    Rank(int hp, int meleeCap, int magicCap, int neoDevilFruitCap, int professionCap, int strength, int endurance, int intelligence, int willpower, int speed, int maxExp, String pirateName, String marineName, String outlawName, int levelReq, int aiDefeatedReq, int pvpFoughtReq) {
        this.hp = hp;
        this.meleeCap = meleeCap;
        this.magicCap = magicCap;
        this.neoDevilFruitCap = neoDevilFruitCap;
        this.professionCap = professionCap;
        this.strength = strength;
        this.endurance = endurance;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.speed = speed;
        this.maxExp = maxExp;
        this.pirateName = pirateName;
        this.marineName = marineName;
        this.outlawName = outlawName;
        this.levelReq = levelReq;
        this.aiDefeatedReq = aiDefeatedReq;
        this.pvpFoughtReq = pvpFoughtReq;
    }

    // Getters and setters
    public int getLevelReq() {
        return levelReq;
    }

    public int getAiDefeatedReq() {
        return aiDefeatedReq;
    }

    public int getPvpFoughtReq() {
        return pvpFoughtReq;
    }

    public int getHp() {
        return hp;
    }

    public int getMeleeCap() {
        return meleeCap;
    }

    public int getMagicCap() {
        return magicCap;
    }

    public int getNeoDevilFruitCap() {
        return neoDevilFruitCap;
    }

    public int getProfessionCap() {
        return professionCap;
    }

    public int getStrength() {
        return strength;
    }

    public int getEndurance() {
        return endurance;
    }

    public String getPirateName() {
        return pirateName;
    }

    public String getMarineName() {
        return marineName;
    }

    public String getOutlawName() {
        return outlawName;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public int getWillpower() {
        return willpower;
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * <p> Checks if the users rank is higher or the same as the supposedLowerRank</p>
     * @param supposedLowerRank The rank the is assumed to be lower
     * @param userRank The current rank of the user
     * @return True if it is the higher or the same, and returns false if otherwise
     */
    public static boolean isHigherOrEqual(Rank supposedLowerRank, Rank userRank) {
        HashMap<Rank, List<Rank>> hashMap = new HashMap<>();
        hashMap.put(R1, Collections.singletonList(R1));
        hashMap.put(R2, List.of(R1, R2));
        hashMap.put(R3, List.of(R1, R2, R3));
        hashMap.put(R4, List.of(R1, R2, R3, R4));
        hashMap.put(R5, List.of(R1, R2, R3, R4, R5));
        hashMap.put(R6, List.of(R1, R2, R3, R4, R5, R6));
        hashMap.put(R7, List.of(R1, R2, R3, R4, R5, R6, R7));
        hashMap.put(R8, List.of(R1, R2, R3, R4, R5, R6, R7, R8));

        return hashMap.get(supposedLowerRank).contains(userRank);
    }
}
