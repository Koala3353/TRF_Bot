package com.general_hello.commands.RPG.Commands.Fight;

import com.general_hello.commands.Bot;
import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.ArrayList;

public class Fight {
    public static ArrayList<Fight> fights = new ArrayList<>();
    private final Fighter fighter1;
    private final Fighter fighter2;
    private boolean turn;
    private long mainMessage;
    private long turnMessage;
    private final long textChannel;

    public Fight(User fighter1, User fighter2, Message mainMessage, Message turnMessage, TextChannel textChannel) {
        this.fighter1 = new Fighter(fighter1);
        this.fighter2 = new Fighter(fighter2);
        this.mainMessage = mainMessage.getIdLong();
        this.turnMessage = turnMessage.getIdLong();
        this.turn = true;
        this.textChannel = textChannel.getIdLong();
    }

    public Fighter getAttackingFighter() {
        return turn ? fighter1 : fighter2;
    }

    public Fighter getDefendingFighter() {
        return !turn ? fighter1 : fighter2;

    }

    public void sendUpdate(int number, String action) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Fighter attackingFighter = getAttackingFighter();
        Fighter defendingFighter = getDefendingFighter();
        embedBuilder.addField(attackingFighter.getUser().getName(), getStatus(attackingFighter.getHealth(), attackingFighter.getShield()),true);
        embedBuilder.setColor(Color.red);
        embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/693517202879414312/860599179578966056/Fight_icon.png");
        embedBuilder.addField(defendingFighter.getUser().getName(), getStatus(defendingFighter.getHealth(), defendingFighter.getShield()),true);
        embedBuilder.addField("Last Action", "`" + attackingFighter.getUser().getName() + " " + action + "`", false);
        Message message = getTurnMessage().editMessage("Your turn " + defendingFighter.getUser().getAsMention()).complete();
        getMainMessage().editMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.primary(defendingFighter.getUser().getId() + ":punch:" + number, "Punch").withEmoji(Emoji.fromMarkdown(RPGEmojis.punch)),
                Button.primary(defendingFighter.getUser().getId() + ":kick:" + number, "Kick").withEmoji(Emoji.fromMarkdown(RPGEmojis.kick)),
                Button.primary(defendingFighter.getUser().getId() + ":defend:" + number, "Defend").withEmoji(Emoji.fromMarkdown(RPGEmojis.defend)),
                Button.danger(defendingFighter.getUser().getId() + ":run:" + number, "Run Away").withEmoji(Emoji.fromMarkdown(RPGEmojis.run_away))
        ).queue(message1 -> {
            setTurnMessage(message.getIdLong());
            setMainMessage(message1.getIdLong());
        });
        this.turn = !this.turn;
    }

    private String getStatus(int health, int levelShield) {
        return "❤ " + RPGDataUtils.getBarFromPercentage(health) + " **" + health + "%**\n" +
                "🛡 " + RPGDataUtils.getBarFromPercentage(levelShield) + " **" + levelShield + " lvl**";
    }

    public Message getTurnMessage() {
        return getTextChannel().retrieveMessageById(this.turnMessage).complete();
    }

    public MessageChannel getTextChannel() {
        return Bot.jda.getTextChannelById(textChannel);
    }

    public Message getMainMessage() {
        return getTextChannel().retrieveMessageById(this.mainMessage).complete();
    }

    public Fight setMainMessage(long mainMessage) {
        this.mainMessage = mainMessage;
        return this;
    }

    public Fight setTurnMessage(long turnMessage) {
        this.turnMessage = turnMessage;
        return this;
    }
}
