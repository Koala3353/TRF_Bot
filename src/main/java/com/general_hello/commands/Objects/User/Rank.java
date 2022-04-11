package com.general_hello.commands.Objects.User;

public enum Rank {
    R1(5_000, 10_000, -1, -1, -1, 5_000, 5_000, 5_000, 5_000, 5_000, 100_000, "Newbie", "Newbie", "Newbie", 0, 0, 0),
    R2(10_000, 50_000, 50_000, -1, -1, 10_000, 10_000, 10_000, 10_000, 10_000, 500_000, "Pirates Trainee", "Marines Trainee", "Street Thug", 5, 25, 0),
    R3(50_000, 100_000, 100_000, -1, -1, 50_000, 50_000, 50_000, 50_000, 50_000, 1_000_000, "Junior Pirates", "Junior Marines", "Gangster", 15, 50, 5),
    R4(100_000, 500_000, 500_000, -1, -1, 100_000, 100_000, 100_000, 100_000, 100_000, 10_000_000, "Senior Pirates", "Senior Marines", "Lesser Outlaw", 25, 100, 15),
    R5(500_000, 1_000_000, 1_000_000, 1_000_000, -1, 500_000, 500_000, 500_000, 500_000, 500_000, 50_000_000, "Elite Pirates", "Elite Marines", "Average Outlaw", 40, 150, 50),
    R6(1_000_000, 1_500_000, 1_500_000, 1_500_000, 1_500_000, 1_000_000, 1_000_000, 1_000_000, 1_000_000, 1_000_000, 100_000_000, "Renowned Pirates", "Renowned Marines", "Greater Outlaw", 55, 500, 70),
    R7(1_500_000, 2_000_000, 2_000_000, 2_000_000, 2_000_000, 1_500_000, 1_500_000, 1_500_000, 1_500_000, 1_500_000, 350_000_000, "Veteran Pirates", "Veteran Marines", "Outlaw Boss", 75, 700, 100),
    R8(2_500_000, 3_000_000, 3_000_000, 3_000_000, 3_000_000, 2_500_000, 2_500_000, 2_500_000, 2_500_000, 2_500_000, 750_000_000, "Legendary Pirates", "Legendary Marines", "Legendary Outlaw", 150, 1_000, 150);

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
}
