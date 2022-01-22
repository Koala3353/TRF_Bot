package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.commands.Emoji.EmojiObject;
import net.dv8tion.jda.api.entities.Emoji;

import java.util.ArrayList;
import java.util.Arrays;

public class RPGEmojis {
    //coins
    public final static String credits = "<:credit:905976767821525042>";
    public final static String igntCoins = "<:ignt_coins:905999722374905857>";
    public final static String shekels = "<:shekels:906039266650505256>";
    public final static String bar1Empty = "<:bar1_empty:917205649694277652>";
    public final static String bar1Half = "<a:bar1_half:917698621510664203>";
    public final static String bar1Full = "<a:bar1_full:913686597088710706>";
    public final static String bar2Empty = "<:bar2_empty:917697677406392361>";
    public final static String bar2Half = "<a:bar2_half:917698621766516736>";
    public final static String bar2Full = "<a:bar2_full:913686585428566056>";
    public final static String bar2High = "<a:bar2_high:925547411676491788>";
    public final static String bar3Empty = "<:bar3_empty:917697757890895872>";
    public final static String bar3Half = "<a:bar3_half:917698621179330560>";
    public final static String bar3Full = "<a:bar3_full:917698622030761984>";
    public final static String punch = "<:punch:926265886384484462>";
    public final static String kick = "<:kick:926267855866064896>";
    public final static String run_away = "<:run:926268031536087071>";
    public final static String defend = "<:defend:926284982631165992>";
    public final static String common_chest = "<a:common_chest:929515512399020033>";
    public final static String uncommon_chest = "<a:uncommon_chest:929516153506758666>";
    public final static String rare_chest = "<a:rare_chest:929516153095729204>";
    public final static String legendary_chest = "<a:legendary_chest:929516153301262418>";
    public final static String mythical_chest = "<a:mythical_chest:929516153422897202>";

    public static EmojiObject customEmojiToEmote(String customEmoji) {
        customEmoji = customEmoji.replace("<", "");
        customEmoji = customEmoji.replace(">", "");

        String[] split = customEmoji.split(":");
        ArrayList<String> splitList = new ArrayList<>(Arrays.asList(split));
        boolean isAnimate = false;
        if (split[0].equals("a")) {
            isAnimate = true;
        }

        System.out.println(splitList);

        return new EmojiObject(splitList.get(1), isAnimate, Long.parseLong(splitList.get(2)));
    }

    public static Emoji customEmojiRawToEmoji(String rawEmojiText) {
        return Emoji.fromMarkdown(rawEmojiText);
    }

    public static String customEmojiUrl(Emoji emoji) {
        if (emoji.isAnimated()) {
            return "https://cdn.discordapp.com/emojis/" + emoji.getId() + ".gif";
        }
        return "https://cdn.discordapp.com/emojis/" + emoji.getId() + ".png";
    }

    /**
     * Change the letter(s) into a String of emojis
     * @param input the letter(s) to be change to emoji
     * @return String of letter(s) in emojis form
     */
    public static String lettersToEmoji(String input)
    {
        String output = ":regional_indicator_" + input.toLowerCase() + ":";
        return output;
    }
}
