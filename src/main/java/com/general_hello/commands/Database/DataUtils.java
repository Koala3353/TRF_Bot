package com.general_hello.commands.Database;

import com.general_hello.commands.Objects.BotEmojis;

import java.text.DecimalFormat;

public class DataUtils {
    public static final DecimalFormat formatter = new DecimalFormat("#,###");

    public static String filter(String filterWord) {
        return filterWord.toLowerCase().replace("'", "").replaceAll("\\s+", "");
    }

    public static String getBarFromPercentage(int percentage) {
        String bar = "";

        if (percentage < 10) {
            bar = BotEmojis.bar1Empty + BotEmojis.bar2Empty + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 20) {
            bar = BotEmojis.bar1Half + BotEmojis.bar2Empty + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 30) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Empty + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 40) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Half + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 50) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2High + BotEmojis.bar2Empty + BotEmojis.bar3Empty;
        } else if (percentage < 65) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Full + BotEmojis.bar2Half + BotEmojis.bar3Empty;
        } else if (percentage < 70) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Full + BotEmojis.bar2High + BotEmojis.bar3Empty;
        } else if (percentage < 85) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Full + BotEmojis.bar2Full + BotEmojis.bar3Half;
        } else if (percentage < 101) {
            bar = BotEmojis.bar1Full + BotEmojis.bar2Full + BotEmojis.bar2Full + BotEmojis.bar3Full;
        }

        return bar;
    }

    public static int getPercentage(int firstNumber, int secondNumber) {
        double solving = (double) firstNumber/secondNumber;
        solving = solving * 100;
        return (int) solving;
    }

    public static String getEmojiFromBoolean(boolean bool) {
        return (bool ? BotEmojis.check : BotEmojis.xmark);
    }

    public static boolean getSettingBooleanFromNumber(int number) {
        return (number == 100);
    }
}