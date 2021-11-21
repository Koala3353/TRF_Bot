package com.general_hello.commands.commands.GroupOfGames.MiniGames;

import com.general_hello.commands.commands.Emoji.EmojiObject;
import com.general_hello.commands.commands.Emoji.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

public class MainChessCode {
    //black
    public static EmojiObject emojiObjectPawn = Emojis.customEmojiToEmote(Emojis.BPAWN);
    public static EmojiObject emojiObjectKing = Emojis.customEmojiToEmote(Emojis.BKING);
    public static EmojiObject emojiObjectQueen = Emojis.customEmojiToEmote(Emojis.BQUEEN);
    public static EmojiObject emojiObjectRook = Emojis.customEmojiToEmote(Emojis.BROOK);
    public static EmojiObject emojiObjectKnight = Emojis.customEmojiToEmote(Emojis.BKNIGHT);
    public static EmojiObject emojiObjectBishop = Emojis.customEmojiToEmote(Emojis.BBISHOP);

    //white
    public static EmojiObject wemojiObjectPawn = Emojis.customEmojiToEmote(Emojis.WPAWN);
    public static EmojiObject wemojiObjectKing = Emojis.customEmojiToEmote(Emojis.WKING);
    public static EmojiObject wemojiObjectQueen = Emojis.customEmojiToEmote(Emojis.WQUEEN);
    public static EmojiObject wemojiObjectRook = Emojis.customEmojiToEmote(Emojis.WROOK);
    public static EmojiObject wemojiObjectKnight = Emojis.customEmojiToEmote(Emojis.WKNIGHT);
    public static EmojiObject wemojiObjectBishop = Emojis.customEmojiToEmote(Emojis.WBISHOP);

    public static void loadBoard(ButtonClickEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setDescription("----------------------------------");

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRows(
                //black
                ActionRow.of(
                        Button.primary(event.getUser().getId() + ":bpawn",
                                Emoji.fromEmote(emojiObjectPawn.getEmojiName(), emojiObjectPawn.getEmojiID(), emojiObjectPawn.isAnimated())),
                        Button.primary(event.getUser().getId() + ":brook",
                                Emoji.fromEmote(emojiObjectRook.getEmojiName(), emojiObjectRook.getEmojiID(), emojiObjectRook.isAnimated())),
                        Button.primary(event.getUser().getId() + ":bknight",
                                Emoji.fromEmote(emojiObjectKnight.getEmojiName(), emojiObjectKnight.getEmojiID(), emojiObjectKnight.isAnimated())),
                        Button.primary(event.getUser().getId() + ":bbishop",
                                Emoji.fromEmote(emojiObjectBishop.getEmojiName(), emojiObjectBishop.getEmojiID(), emojiObjectBishop.isAnimated())),
                        Button.primary(event.getUser().getId() + ":bqueen",
                                Emoji.fromEmote(emojiObjectQueen.getEmojiName(), emojiObjectQueen.getEmojiID(), emojiObjectQueen.isAnimated()))),
                //white
                ActionRow.of(Button.secondary(event.getUser().getId() + ":wpawn",
                        Emoji.fromEmote(wemojiObjectPawn.getEmojiName(), wemojiObjectPawn.getEmojiID(), emojiObjectPawn.isAnimated())).asDisabled(),
                        Button.secondary(event.getUser().getId() + ":wrook",
                                Emoji.fromEmote(wemojiObjectRook.getEmojiName(), wemojiObjectRook.getEmojiID(), wemojiObjectRook.isAnimated())).asDisabled(),
                        Button.secondary(event.getUser().getId() + ":wknight",
                                Emoji.fromEmote(wemojiObjectKnight.getEmojiName(), wemojiObjectKnight.getEmojiID(), wemojiObjectKnight.isAnimated())).asDisabled(),
                        Button.secondary(event.getUser().getId() + ":wbishop",
                                Emoji.fromEmote(wemojiObjectBishop.getEmojiName(), wemojiObjectBishop.getEmojiID(), wemojiObjectBishop.isAnimated())).asDisabled(),
                        Button.secondary(event.getUser().getId() + ":wqueen",
                                Emoji.fromEmote(wemojiObjectQueen.getEmojiName(), wemojiObjectQueen.getEmojiID(), wemojiObjectQueen.isAnimated())).asDisabled()),
                //king
                ActionRow.of(
                        Button.danger(event.getUser().getId() + ":bking",
                                Emoji.fromEmote(emojiObjectKing.getEmojiName(), emojiObjectKing.getEmojiID(), emojiObjectKing.isAnimated())),

                        Button.danger(event.getUser().getId() + ":wking",
                                Emoji.fromEmote(wemojiObjectKing.getEmojiName(), wemojiObjectKing.getEmojiID(), wemojiObjectKing.isAnimated())).asDisabled())).queue();
    }
}
