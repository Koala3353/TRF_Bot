package com.general_hello.commands.Objects;

public enum EffectTypes {
    AOE("AOE", "Since only Tournament mode can do 2v2 or 3v3, this means that if they funnily choose to equip on Ai, Normal or Ranked (not to be confused by Rx, as stated in main document) matches means will just hit 1 enemy instead"),
    NEO_DEVIL_FRUIT_SEALING("Neo Devil Fruit Sealing/Sealing skill", "This skills simply means just disabling/stunning Neo Devil Fruit skills for x amount of turns during the matches. Meaning that if players have Neo Devil Fruits and can buy or have bought the skills from shop then equip and bring to Ai, Normal PvP, Ranked or Tournament and meet an opponent with this skill and they use it, their equipped Neo Devil Fruit skill will be stunned for x amount of turns"),
    MELEE_MAGIC_OR_DUAL_DAMAGE_DEBUFF("Melee, Magic or Dual Damage Debuff (Opponent)", "Reduce opponent's melee and magic attack stats"),
    MELEE_MAGIC_DUAL_DAMAGE_SELF_ENHANCE("Melee, Magic or Dual Damage Self Enhance", "Melee, Magic or Dual Damage Self Enhance"),
    MELEE_MAGIC_DUAL_DEFENSE_DEBUFF("Melee, Magic, Dual Defense Debuff (Opponent)", "Melee, Magic, Dual Defense Debuff (Opponent)"),
    MELEE_MAGIC_DUAL_DEFENSE_SELFENHANCE("Melee, Magic, Dual Defense Self Enhance", "Melee, Magic, Dual Defense Self Enhance"),
    REFLECT_SEALING("Reflect/Reflect Sealing", "Melee, Magic or Dual Damage Self Enhance"),
    HEAL("Heal/Heal Overtime/Heal Reduction (or Sealing)", "Melee, Magic or Dual Damage Self Enhance"),
    DISSIPITATE("Dissipate", "Deals more damage to Aoe Residual/Affliction skills and Remove field aoe or effects after"),
    IMMOLATE("Immolate", "Convert Residual/Affliction to instant damage and remove the Residual/Affliction effect after"),
    MAIM("Maim", "Cause opponent to take Residua/Affliction Damage each time they use/click skill/battle interaction button; in other words, does something"),
    BARRIER("Barrier", "Desctructible/Indestructible, Destructible/Indestructible Barrier Seal"),
    RECOIL("Recoil", "Increase damage deal by this type of skills but will deal Self Damage too"),
    RESIDUAL("Residual/Affliction", "Cause the opponent to take damage for certain amount of rounds"),
    COMPOUND_RESIDUAL("Compound Residual/Affliction", "Same as normal Residual/Affliction but skills of this type will increase its Residual/Affliction damage over time for certain number of rounds till the skill finishes"),
    HP_DRAIN("HP/Energy drain to self", "Suicide pill?"),
    ENERGY_REDUCTION("Energy reduction opponent", "Energy reduction to opponent"),
    DISSIPATE_ENHANCE("Dissipate Enhance/Debuff", "Some info"),
    DAMAGE("Damage/Piercing Damage", "Pierces to all barriers"),
    STUN("Stun", "Stun"),
    DROWN("Drown or Poison", ""),
    END_ROUND_IN_TURNS("End in x (number) of turns and win/lose/draw", "End in x (number) of turns and win/lose/draw"),
    STATS_SWAP("Stats swap", ""),
    IGNORE_BUFF_DAMAGE("Ignore buff/debuff/damages for x turns", "Ignore buff/debuff/damages for x turns"),
    SELF_DESTRUCT("Self Destruct", "Self Destruct (Do massive damage and instantly kill both opponents and user)"),
    COPY_SKILLS("Copy skills for x turns", "The title"),
    INCREASE_SKILL_TURNS("Increase skill turns", "Title");

    private final String name;
    private final String description;

    EffectTypes(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
