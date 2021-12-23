package com.general_hello.commands.RPG.Items;

import com.general_hello.commands.RPG.Objects.*;
import com.general_hello.commands.RPG.Types.AttainableBy;
import com.general_hello.commands.RPG.Types.Rarity;

import java.util.ArrayList;
import java.util.HashMap;

public class Initializer {
    public static HashMap<String, LandAnimal> landAnimalToId = new HashMap<>();
    public static HashMap<String, SeaAnimal> seaAnimalToId = new HashMap<>();
    public static HashMap<String, Artifact> artifactToId = new HashMap<>();
    public static HashMap<String, Tool> toolToId = new HashMap<>();
    public static HashMap<String, Objects> allItems = new HashMap<>();
    public static HashMap<String, Animal> allAnimals = new HashMap<>();
    public static HashMap<String, Weapon> weaponToId = new HashMap<>();
    public static ArrayList<LandAnimal> landAnimals = new ArrayList<>();
    public static ArrayList<String> animals = new ArrayList<>();
    public static ArrayList<SeaAnimal> seaAnimals = new ArrayList<>();
    public static ArrayList<Artifact> artifacts = new ArrayList<>();
    public static ArrayList<Tool> tools = new ArrayList<>();
    public static ArrayList<Weapon> weapons = new ArrayList<>();
    public static ArrayList<String> allNames = new ArrayList<>();
    public static ArrayList<SeaAnimal> commonSea = new ArrayList<>();
    public static ArrayList<SeaAnimal> uncommonSea = new ArrayList<>();
    public static ArrayList<SeaAnimal> rareSea = new ArrayList<>();
    public static ArrayList<SeaAnimal> legendarySea = new ArrayList<>();
    public static ArrayList<SeaAnimal> mythicalSea = new ArrayList<>();
    public static ArrayList<LandAnimal> commonLand = new ArrayList<>();
    public static ArrayList<LandAnimal> uncommonLand = new ArrayList<>();
    public static ArrayList<LandAnimal> rareLand = new ArrayList<>();
    public static ArrayList<LandAnimal> legendaryLand = new ArrayList<>();
    public static ArrayList<LandAnimal> mythicalLand = new ArrayList<>();

    public static void initializer() {
        //Fish
        //Common
        new SeaAnimal(Rarity.COMMON, "Common fish", "<:common_fish:911077702474268722>");
        new SeaAnimal(Rarity.COMMON, "Seaweed", "<:seaweed:899872673419644968>");
        //Uncommon
        new SeaAnimal(Rarity.UNCOMMON, "Uncommon Fish", "<:uncommon_fish:923382221539254323>");
        new SeaAnimal(Rarity.UNCOMMON, "Peter's Fish", "<:fishy:899492725798354946>");
        //Rare
        new SeaAnimal(Rarity.RARE, "Rare Fish", "<:rare_fish:923382338648428555>");
        new SeaAnimal(Rarity.RARE, "Jelly Fish", "<:jellyfish:899873055084511302>");
        new SeaAnimal(Rarity.RARE, "Cuttle Fish", "");
        new SeaAnimal(Rarity.RARE, "Puffer Fish", "");
        new SeaAnimal(Rarity.RARE, "Shrimp", "");
        //Legendary
        new SeaAnimal(Rarity.LEGENDARY, "Legendary Fish", "<:leggy_fish:911077765292363776>");
        new SeaAnimal(Rarity.LEGENDARY, "Octopus", "<:octopuses:899872869322989589>");
        new SeaAnimal(Rarity.LEGENDARY, "Crab", "<a:crabby:900170344202113096>", "Powerful yet small, a crab is.", 1_000_000);
        new SeaAnimal(Rarity.LEGENDARY, "Lobster", "", "Brother of crab.", 1_200_000);
        new SeaAnimal(Rarity.LEGENDARY, "Jonah's Big Fish", "<:jonah_big_fish:899872152961044521>");
        //Mythical
        new SeaAnimal(Rarity.MYTHICAL, "Mythical Fish", "<:mythical_fish:923382428981162004>", "Impossible to have one if ye are poor", 20_000_000);
        new SeaAnimal(Rarity.MYTHICAL, "Kraken", "<:kraken:899871980017311795>", "No one knows what this creature is!", 50_000_000);

        //Hunt
        //Common
        new LandAnimal(Rarity.COMMON, "Gnat", "<:gnat:917609948379250768>");
        new LandAnimal(Rarity.COMMON, "Locust", "");
        new LandAnimal(Rarity.COMMON, "Goat", "<:goaty:911076804972937286>");
        new LandAnimal(Rarity.COMMON, "Donkey", "<:donkey:911158404293210112>");
        new LandAnimal(Rarity.COMMON, "Dove", "<:doves:899873583587803147>");
        //Uncommon
        new LandAnimal(Rarity.UNCOMMON, "Pig", "<:piggy:899873330113437766>");
        new LandAnimal(Rarity.UNCOMMON, "Dog", "<:doggy:914673871901294622>");
        new LandAnimal(Rarity.UNCOMMON, "Sheep", "<:sheeps:914747699008520263>");
        new LandAnimal(Rarity.UNCOMMON, "Cow", "<:cowy:911154081937117245>");
        new LandAnimal(Rarity.UNCOMMON, "Eagle", "<:eagles:911077025220005949>");
        //Rare
        new LandAnimal(Rarity.RARE, "Camel", "<:camely:911397794005254146>");
        new LandAnimal(Rarity.RARE, "Sparrow", "<:sparrow:911746535396638781>");
        new LandAnimal(Rarity.RARE, "Lion", "<:liony:911076910598086666>");
        new LandAnimal(Rarity.RARE, "Serpent", "");
        //Legendary
        new LandAnimal(Rarity.LEGENDARY, "Dinosaur", "<:dinosaur:905241832550699099>", "The most delicious animal!", 1_000_000);
        //Mythical
        new LandAnimal(Rarity.MYTHICAL, "Rhino", "", "A mythical creature only seen and eaten by the richest of the richest", 10_000_000);

        //Tools
        new Tool(Rarity.COMMON, "Fishing Pole", AttainableBy.FISHING, "<:fishing_pole:899872035289833522>", "This fishing pole allows you to use the fish command, which can give you lots of different types of items!");
        new Tool(Rarity.COMMON, "Hunting Rifle", AttainableBy.HUNTING, "<:hunting_riffle:899871918025486386>", "This hunting rifle allows you to use the hunt command, which can give you lots of different types of items!");

        //Artifacts
        new Artifact(Rarity.COMMON, "Clay Pot", "<:clay_pot:899872733758898227>", "A pot that is filled with clay", 200);
        new Artifact(Rarity.COMMON, "Sack Cloth", "<:sack:899873147048820748>", "A sack made of cloth", 700);
        new Artifact(Rarity.UNCOMMON, "Wine Skin", "<:wine_skin:912589755668111421>", "A wine skin. Whatever that is.", 10_000);
        new Artifact(Rarity.RARE, "Ruby", "<:ruby:899873184554315816>", "A beautiful ruby for a special person 😉", 25_000);
        new Artifact(Rarity.RARE, "Stones of David", "<:david_stones:899873415098417162>", "The five stones used by David to kill Goliath", 40_000);
        new Artifact(Rarity.RARE, "Coat of Joseph", "<:robe:914676817036705863>", "A colorful coat given to Joseph then given to you", 70_000);
        new Artifact(Rarity.LEGENDARY, "Diamond", "<:diamond:899873834549780512>", "Diamond... A gem.", 150_000);
        new Artifact(Rarity.LEGENDARY, "Rod of Moses", "<:moses_rod:905961435891372083>", "A rod used by Moses to perform miracles.", 500_000);
        new Artifact(Rarity.MYTHICAL, "Part of Ten Commandments", "<:tencommandment:905241910644461569>", "The Ten Commandments. Nothing more. Nothing less.", 2_000_000);
        backEndInitializer();
    }

    private static void backEndInitializer() {
        ArrayList<SeaAnimal> seaAnimals = Initializer.seaAnimals;
        int x = 0;
        while (x < seaAnimals.size()) {
            SeaAnimal seaAnimal = seaAnimals.get(x);
            switch (seaAnimal.getRarity()) {
                case COMMON:
                    commonSea.add(seaAnimal);
                    break;
                case UNCOMMON:
                    uncommonSea.add(seaAnimal);
                    break;
                case RARE:
                    rareSea.add(seaAnimal);
                    break;
                case LEGENDARY:
                    legendarySea.add(seaAnimal);
                    break;
                case MYTHICAL:
                    mythicalSea.add(seaAnimal);
                    break;
            }
            x++;
        }

        ArrayList<LandAnimal> landAnimals = Initializer.landAnimals;
        x = 0;
        while (x < landAnimals.size()) {
            LandAnimal landAnimal = landAnimals.get(x);
            switch (landAnimal.getRarity()) {
                case COMMON:
                    commonLand.add(landAnimal);
                    break;
                case UNCOMMON:
                    uncommonLand.add(landAnimal);
                    break;
                case RARE:
                    rareLand.add(landAnimal);
                    break;
                case LEGENDARY:
                    legendaryLand.add(landAnimal);
                    break;
                case MYTHICAL:
                    mythicalLand.add(landAnimal);
                    break;
            }
            x++;
        }
    }
}
