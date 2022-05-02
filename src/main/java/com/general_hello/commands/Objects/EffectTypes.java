package com.general_hello.commands.Objects;

public enum EffectTypes {
    // The effects (Fully documented will be on stage 4)
    AOE("AOE"),
    NEO_DEVIL_FRUIT_SEALING("Neo Devil Fruit Sealing/Sealing skill"),
    SKILL_SEALING("Skill Sealing"),
    MELEE_DAMAGE_DEBUFF("Melee Damage Debuff (Opponent)"),
    MAGIC_DAMAGE_DEBUFF("Magic Damage Debuff (Opponent)"),
    DUAL_DAMAGE_DEBUFF("Dual Damage Debuff (Opponent)"),
    MELEE_SELF_ENHANCE("Melee Damage Self Enhance"),
    MAGIC_SELF_ENHANCE("Magic Damage Self Enhance"),
    DUAL_DAMAGE_SELF_ENHANCE("Dual Damage Self Enhance"),
    MELEE_DEFENSE_DEBUFF("Melee Defense Debuff (Opponent)"),
    MAGIC_DEFENCE_DEBUFF("Melee Defense Debuff (Opponent)"),
    DUAL_DEFENSE_DEBUFF("Dual Defense Debuff (Opponent)"),
    MELEE_DEFENSE_SELFENHANCE("Melee Defense Self Enhance"),
    MAGIC_SELFENHANCE("Magic Defense Self Enhance"),
    DUAL_DEFENSE_SELFENHANCE("Dual Defense Self Enhance"),
    REFLECT("Reflect"),
    REFLECT_SEALING("Reflect Sealing"),
    HEAL("Heal"),
    HEAL_OVERTIME("Heal Overtime"),
    HEAL_REDUCTION("Heal Reduction"),
    HEAL_SEALING("Heal Sealing"),
    DISSIPITATE("Dissipate"),
    IMMOLATE("Immolate"),
    MAIM("Maim"),
    DESTRUCTIBLE_BARRIER("Destructible Barrier"),
    INDESTRUCTIBLE_BARRIER("Indestructible Barrier"),
    DESTRUCTIBLE_BARRIER_SEAL("Destructible Barrier Seal"),
    INDESTRUCTIBLE_BARRIER_SEAL("Indestructible Barrier Seal"),
    RECOIL("Recoil"),
    SUMMON("Summon"),
    TEMPORARY_SUMMON("Temporary Summon"),
    RESIDUAL("Residual/Affliction"),
    COMPOUND_RESIDUAL("Compound Residual/Affliction"),
    HP_DRAIN("HP drain to self"),
    ENERGY_DRAIN("Energy Drain"),
    ENERGY_INCREASE_TO_SELF("Energy Increase to Self by x amount"),
    ENERGY_INCREASE_TO_ALLY("Energy Increase to Ally by x amount"),
    ENERGY_REDUCTION("Energy reduction"),
    DISSIPATE_ENHANCE("Dissipate Enhance"),
    DISSIPATE_DEBUFF("Dissipate Debuff"),
    DAMAGE("Damage"),
    PIERCING_DAMAGE("Piercing Damage"),
    STUN("Stun"),
    DROWN("Drown"),
    POISON("Poison"),
    BLEEDING("Bleeding"),
    END_ROUND_IN_TURNS_WIN("End in x of turns and win instantly"),
    END_ROUND_IN_TURNS_DRAW("End in x of turns and draw instantly"),
    STATS_SWAP("Stats swap"),
    IGNORE_BUFF("Ignore buff for x turns"),
    IGNORE_DEBUFF("Ignore debuff for x turns"),
    IGNORE_DAMAGE("Ignore damages for x turns"),
    SELF_DESTRUCT("Self Destruct"),
    COPY_SKILLS("Copy skills for x turns"),
    INCREASE_SKILL_TURNS("Increase skill turns"),
    DECREASE_SKILL_TURNS("Decrease skill turns");

    // Variable
    private final String name;

    // Initializer
    EffectTypes(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {
        return name;
    }

}
