package com.general_hello.commands.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class EmbedUtil
{
    /**
     * A class for making repetitive embeds using {@link EmbedBuilder}.
     *
     * <p>This class is used to make embeds
     *
     * @author General Rain
     */

    // If you want to change the color and emojis simply edit the ones below
    public static final int DEFAULT_COLOR = 0x0099E1;

    public static final int SUCCESS_COLOR = 0x0099E1 ;
    public static final int ERROR_COLOR = 0x790604;
    public static final int WARNING_COLOR = 0xFFEA17;

    public static final String SUCCESS_UNICODE = "✅";
    public static final String ERROR_UNICODE = "❌";
    public static final String WARNING_UNICODE = "⚠";
    public static final String NO_ENTRY_UNICODE = "⛔";

    // Default embed
    public static MessageEmbed defaultEmbed(String content, Color color)
    {
        return new EmbedBuilder()
                .setDescription(content)
                .setColor(color)
                .build();
    }

    public static MessageEmbed defaultEmbed(String content, int color)
    {
        return new EmbedBuilder()
                .setDescription(content)
                .setColor(color)
                .build();
    }

    public static MessageEmbed defaultEmbed(String content, byte r, byte g, byte b)
    {
        return new EmbedBuilder()
                .setDescription(content)
                .setColor(rgbToInt(r, g, b))
                .build();
    }


    public static MessageEmbed defaultEmbed(String content)
    {
        return new EmbedBuilder()
                .setDescription(content)
                .setColor(DEFAULT_COLOR)
                .build();
    }

    public static MessageEmbed defaultEmbed(String content, String image)
    {
        return new EmbedBuilder()
                .setDescription(content)
                .setColor(DEFAULT_COLOR)
                .setImage(image)
                .build();
    }

    public static MessageEmbed defaultEmbed(String content, String image, String thumbnail, Color color)
    {
        return new EmbedBuilder()
                .setDescription(content)
                .setImage(image)
                .setThumbnail(thumbnail)
                .setColor(color)
                .build();
    }

    public static MessageEmbed defaultEmbed(String title, String content, String image, String thumbnail)
    {
        return new EmbedBuilder()
                .setDescription(content)
                .setTitle(title)
                .setImage(image)
                .setThumbnail(thumbnail)
                .setColor(SUCCESS_COLOR)
                .build();
    }

    public static EmbedBuilder defaultEmbedBuilder(String content)
    {
        return new EmbedBuilder()
                .setDescription(content)
                .setColor(DEFAULT_COLOR);
    }

    public static MessageEmbed errorEmbed(String content)
    {
        return new EmbedBuilder()
                .setDescription(ERROR_UNICODE + " " + content)
                .setColor(ERROR_COLOR)
                .build();
    }

    public static MessageEmbed warningEmbed(String content)
    {
        return new EmbedBuilder()
                .setDescription(WARNING_UNICODE + " " + content)
                .setColor(WARNING_COLOR)
                .build();
    }

    public static MessageEmbed successEmbed(String content)
    {
        return new EmbedBuilder()
                .setDescription(SUCCESS_UNICODE + " " + content)
                .setColor(SUCCESS_COLOR)
                .build();
    }

    public static MessageEmbed noEntryEmbed(String content)
    {
        return new EmbedBuilder()
                .setDescription(NO_ENTRY_UNICODE + " " + content)
                .setColor(ERROR_COLOR)
                .build();
    }

    public static EmbedBuilder errorEmbedBuilder(String content)
    {
        return new EmbedBuilder()
                .setDescription(ERROR_UNICODE + " " + content)
                .setColor(ERROR_COLOR);
    }

    public static EmbedBuilder warningEmbedBuilder(String content)
    {
        return new EmbedBuilder()
                .setDescription(WARNING_UNICODE + " " + content)
                .setColor(WARNING_COLOR);
    }

    public static EmbedBuilder successEmbedBuilder(String content)
    {
        return new EmbedBuilder()
                .setDescription(SUCCESS_UNICODE + " " + content)
                .setColor(SUCCESS_COLOR);
    }

    public static EmbedBuilder noEntryEmbedBuilder(String content)
    {
        return new EmbedBuilder()
                .setDescription(NO_ENTRY_UNICODE + " " + content)
                .setColor(ERROR_COLOR);
    }

    // RGB to int
    public static int rgbToInt(byte r, byte g, byte b)
    {
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }

    /**
     * A method for changing {@link Integer} to {@link Color}.
     *
     * @author General Rain
     */
    public static Color intToColor(int rgb)
    {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new Color(red, green, blue);
    }
}
