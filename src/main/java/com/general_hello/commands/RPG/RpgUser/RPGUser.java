package com.general_hello.commands.RPG.RpgUser;

import com.general_hello.commands.RPG.Items.Common.Artifacts.ClayPot;
import com.general_hello.commands.RPG.Items.Common.Artifacts.SackCloth;
import com.general_hello.commands.RPG.Items.Common.AttainFish.EgyptianBrokenWeapon;
import com.general_hello.commands.RPG.Items.Common.AttainFish.FishCommon;
import com.general_hello.commands.RPG.Items.Common.AttainFish.Garbage;
import com.general_hello.commands.RPG.Items.Common.AttainFish.SeaWeed;
import com.general_hello.commands.RPG.Items.Common.AttainHunt.*;
import com.general_hello.commands.RPG.Items.Legendary.Artifacts.*;
import com.general_hello.commands.RPG.Items.Legendary.AttainFish.Kraken;
import com.general_hello.commands.RPG.Items.Legendary.AttainFish.LegendaryFish;
import com.general_hello.commands.RPG.Items.Legendary.AttainFish.Leviathan;
import com.general_hello.commands.RPG.Items.Legendary.AttainHunt.Unicorn;
import com.general_hello.commands.RPG.Items.Mythical.Artifacts.MosesRod;
import com.general_hello.commands.RPG.Items.Mythical.AttainFish.Lobster;
import com.general_hello.commands.RPG.Items.Mythical.AttainFish.Octopus;
import com.general_hello.commands.RPG.Items.Mythical.AttainFish.Shark;
import com.general_hello.commands.RPG.Items.Mythical.AttainFish.SoulFish;
import com.general_hello.commands.RPG.Items.Mythical.AttainHunt.Dinosaur;
import com.general_hello.commands.RPG.Items.Rare.Artifacts.*;
import com.general_hello.commands.RPG.Items.Rare.AttainFish.*;
import com.general_hello.commands.RPG.Items.Rare.AttainHunt.Camel;
import com.general_hello.commands.RPG.Items.Rare.AttainHunt.Lion;
import com.general_hello.commands.RPG.Items.Rare.AttainHunt.Serpent;
import com.general_hello.commands.RPG.Items.Uncommon.Artifacts.AmmoriteOldWeapon;
import com.general_hello.commands.RPG.Items.Uncommon.Artifacts.WineSkin;
import com.general_hello.commands.RPG.Items.Uncommon.AttainFish.BoxOfSand;
import com.general_hello.commands.RPG.Items.Uncommon.AttainFish.FishUncommon;
import com.general_hello.commands.RPG.Items.Uncommon.AttainFish.FishermanNet;
import com.general_hello.commands.RPG.Items.Uncommon.AttainFish.PetersFish;
import com.general_hello.commands.RPG.Items.Uncommon.AttainHunt.Cow;
import com.general_hello.commands.RPG.Items.Uncommon.AttainHunt.Eagle;
import com.general_hello.commands.RPG.Items.Uncommon.AttainHunt.Sheep;
import com.general_hello.commands.RPG.Items.Uncommon.AttainHunt.Sparrow;
import com.general_hello.commands.RPG.Items.Uncommon.FishingPole;
import com.general_hello.commands.RPG.Items.Uncommon.HuntingRifle;
import com.general_hello.commands.RPG.Objects.*;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class RPGUser {
    private User user;
    private int shekels;
    private int rpgLevel;

    //Artifacts
    private HashMap<LandAnimals, Integer> landAnimals;
    private HashMap<SeaAnimals, Integer> seaAnimals;
    //AttainFish
    private HashMap<Tools, Integer> tools;
    private HashMap<Weapons, Integer> weapons;
    private HashMap<Artifacts, Integer> artifacts;

    public RPGUser(User user) {
        this.user = user;
        this.shekels = 1000;
        this.rpgLevel = 1;

        landAnimals = new HashMap<>();
        seaAnimals = new HashMap<>();
        weapons = new HashMap<>();
        tools = new HashMap<>();
        artifacts = new HashMap<>();

        startAllHashmap();
    }

    private void startAllHashmap() {
        //legendary
        artifacts.put( new EightCommandment() , 0);
        artifacts.put( new FifthCommandment() , 0);
        artifacts.put( new FirstCommandment() , 0);
        artifacts.put( new FourthCommandment() , 0);
        artifacts.put( new NinthCommandment() , 0);
        artifacts.put( new SecondCommandment() , 0);
        artifacts.put( new SeventhCommandment() , 0);
        artifacts.put( new SixthCommandment() , 0);
        artifacts.put( new TenthCommandment() , 0);
        artifacts.put( new ThirdCommandment() , 0);
        seaAnimals.put( new Kraken() , 0);
        seaAnimals.put( new LegendaryFish() , 0);
        seaAnimals.put( new Leviathan() , 0);
        landAnimals.put( new Unicorn() , 0);
        //mythical
        artifacts.put(new MosesRod(), 0);
        seaAnimals.put(new Lobster(), 0);
        seaAnimals.put(new Octopus(), 0);
        seaAnimals.put(new Shark(), 0);
        seaAnimals.put(new SoulFish(), 0);
        landAnimals.put(new Dinosaur(), 0);
        //rare
        artifacts.put(new DavidsFifthStone(), 0);
        artifacts.put(new DavidsFirstStone(), 0);
        artifacts.put(new DavidsFourthStone(), 0);
        artifacts.put(new DavidsSecondStone(), 0);
        artifacts.put(new DavidsThirdStone(), 0);
        artifacts.put(new Diamond(), 0);
        artifacts.put(new JosephsCoat(), 0);
        artifacts.put(new Ruby(), 0);
        seaAnimals.put(new CuttleFish(), 0);
        seaAnimals.put(new ExoticFish(), 0);
        seaAnimals.put(new JellyFish(), 0);
        seaAnimals.put(new JonahBigFish(), 0);
        seaAnimals.put(new PufferFish(), 0);
        seaAnimals.put(new Shrimp(), 0);
        seaAnimals.put(new Squid(), 0);
        landAnimals.put(new Camel(), 0);
        landAnimals.put(new Lion(), 0);
        landAnimals.put(new Serpent(), 0);
        //uncommon
        artifacts.put(new AmmoriteOldWeapon(), 0);
        artifacts.put(new WineSkin(), 0);
        seaAnimals.put(new BoxOfSand(), 0);
        seaAnimals.put(new FishermanNet(), 0);
        seaAnimals.put(new FishUncommon(), 0);
        seaAnimals.put(new PetersFish(), 0);
        landAnimals.put(new Cow(), 0);
        landAnimals.put(new Eagle(), 0);
        landAnimals.put(new Sheep(), 0);
        landAnimals.put(new Sparrow(), 0);
        tools.put(new FishingPole(), 0);
        tools.put(new HuntingRifle(), 0);
        //common
        artifacts.put(new ClayPot(), 0);
        artifacts.put(new SackCloth(), 0);
        weapons.put(new EgyptianBrokenWeapon(), 0);
        seaAnimals.put(new FishCommon(), 0);
        seaAnimals.put(new Garbage(), 0);
        seaAnimals.put(new SeaWeed(), 0);
        landAnimals.put(new Dog(), 0);
        landAnimals.put(new Donkey(), 0);
        landAnimals.put(new Dove(), 0);
        landAnimals.put(new Gnat(), 0);
        landAnimals.put(new Goat(), 0);
        landAnimals.put(new Locust(), 0);
        landAnimals.put(new Pig(), 0);
    }

    public User getUser() {
        return user;
    }

    public RPGUser setUser(User user) {
        this.user = user;
        return this;
    }

    public int getShekels() {
        return shekels;
    }

    public RPGUser setShekels(int shekels) {
        this.shekels = shekels;
        return this;
    }

    public int getRpgLevel() {
        return rpgLevel;
    }

    public RPGUser setRpgLevel(int rpgLevel) {
        this.rpgLevel = rpgLevel;
        return this;
    }

    public HashMap<LandAnimals, Integer> getLandAnimals() {
        return landAnimals;
    }

    public int getLandAnimalSpecificCount(LandAnimals landAnimals) {
        return this.landAnimals.get(landAnimals);
    }

    public int getSeaAnimalSpecificCount(SeaAnimals seaAnimals) {
        return this.seaAnimals.get(seaAnimals);
    }

    public int getToolSpecificCount(Tools tools) {
        return this.tools.get(tools);
    }

    public RPGUser setLandAnimals(HashMap<LandAnimals, Integer> landAnimals) {
        this.landAnimals = landAnimals;
        return this;
    }

    public HashMap<SeaAnimals, Integer> getSeaAnimals() {
        return seaAnimals;
    }

    public RPGUser setSeaAnimals(HashMap<SeaAnimals, Integer> seaAnimals) {
        this.seaAnimals = seaAnimals;
        return this;
    }

    public HashMap<Tools, Integer> getTools() {
        return tools;
    }

    public RPGUser setTools(HashMap<Tools, Integer> tools) {
        this.tools = tools;
        return this;
    }

    public HashMap<Weapons, Integer> getWeapons() {
        return weapons;
    }

    public int getWeaponsSpecificCount(Weapons weapons) {
        return this.weapons.get(weapons);
    }

    public RPGUser setWeapons(HashMap<Weapons, Integer> weapons) {
        this.weapons = weapons;
        return this;
    }

    public HashMap<Artifacts, Integer> getArtifacts() {
        return artifacts;
    }

    public RPGUser setArtifacts(HashMap<Artifacts, Integer> artifacts) {
        this.artifacts = artifacts;
        return this;
    }

    public int getArtifactsSpecificCount(Artifacts artifacts) {
        return this.artifacts.get(artifacts);
    }
}
