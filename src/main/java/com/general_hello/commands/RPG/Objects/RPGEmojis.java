package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.commands.Emoji.EmojiObject;

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
