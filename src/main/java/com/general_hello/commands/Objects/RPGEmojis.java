package com.general_hello.commands.Objects;

import net.dv8tion.jda.api.entities.Emoji;

public class RPGEmojis {
    //coins
    public final static String berri = "<:credit:905976767821525042>";
    public final static String rainbowShards = "<:shekels:906039266650505256>";
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
    public final static String devs_chest = "<a:devs_chest:941206388170969098>";

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
