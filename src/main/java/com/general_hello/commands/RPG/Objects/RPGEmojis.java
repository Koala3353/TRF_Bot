package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.commands.Emoji.EmojiObject;

import java.util.ArrayList;
import java.util.Arrays;

public class RPGEmojis {
    //coins
    public final static String credits = "<:credit:905976767821525042>";
    public final static String igntCoins = "<:ignt_coins:905999722374905857>";
    public final static String shekels = "<:shekels:906039266650505256>";

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
