package com.general_hello.commands.Objects.Trade;

import com.general_hello.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.HashMap;

public class Trade {
    // Variables
    private static final HashMap<Long, Trade> trades = new HashMap<>();
    private final long proposerUserId;
    private final long targetUserId;
    private Offer proposerOffer;
    private Offer targetToGiveOffer;
    private boolean acceptedProposer;
    private boolean acceptedTarget;
    private int focusedOffer;

    // Initializer
    public Trade(long proposerUserId, long targetUserId, Offer proposerOffer, Offer targetToGiveOffer) {
        this.proposerUserId = proposerUserId;
        this.targetUserId = targetUserId;
        this.proposerOffer = proposerOffer;
        this.targetToGiveOffer = targetToGiveOffer;
        this.acceptedProposer = false;
        this.acceptedTarget = false;
        this.focusedOffer = 1;
        trades.put(proposerUserId, this);
        trades.put(targetUserId, this);
    }

    // Saves the trade in the hashmap
    public Trade save() {
        trades.put(proposerUserId, this);
        trades.put(targetUserId, this);
        return this;
    }

    // Getters and setters
    public Offer getFocusedOffer() {
        return this.focusedOffer == 1 ? getProposerOffer() : getTargetToGiveOffer();
    }

    public Trade setOfferFocused(Offer offer) {
        return this.focusedOffer == 1 ? setProposerOffer(offer) : setTargetToGiveOffer(offer);
    }

    public User getFocusedOfferTarget() {
        return this.focusedOffer == 1 ? getTargetUser() : getProposerUser();
    }

    public Trade switchFocusedOffer() {
        this.focusedOffer = (this.focusedOffer == 1 ? 2 : 1);
        return this;
    }

    public static Trade loadTrade(long userId) {
        if (!trades.containsKey(userId)) return null;
        return trades.get(userId);
    }

    public void deleteTrade() {
        trades.remove(proposerUserId);
        trades.remove(targetUserId);
    }

    public Trade resetTrade() {
        return new Trade(proposerUserId, targetUserId, new Offer(), new Offer()).save();
    }

    public static Trade loadTrade(String userId) {
        if (!trades.containsKey(Long.parseLong(userId))) return null;
        return trades.get(Long.parseLong(userId));
    }

    public long getProposerUserId() {
        return proposerUserId;
    }

    public User getProposerUser() {
        return Bot.jda.getUserById(this.proposerUserId);
    }

    public Trade setProposerOffer(Offer proposerOffer) {
        this.proposerOffer = proposerOffer;
        return this;
    }

    public User getTargetUser() {
        return Bot.jda.getUserById(this.targetUserId);
    }

    public Trade setTargetToGiveOffer(Offer targetToGiveOffer) {
        this.targetToGiveOffer = targetToGiveOffer;
        return this;
    }

    public boolean isAcceptedProposer() {
        return acceptedProposer;
    }

    public boolean isAcceptedTarget() {
        return acceptedTarget;
    }

    public boolean isAccepted() {
        return (this.acceptedProposer && this.acceptedTarget);
    }

    public Trade setAcceptedProposer() {
        this.acceptedProposer = true;
        return this;
    }

    public Trade setAcceptedTarget() {
        this.acceptedTarget = true;
        return this;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public Offer getProposerOffer() {
        return proposerOffer;
    }

    public Offer getTargetToGiveOffer() {
        return targetToGiveOffer;
    }

    // Makes the embed of the trade
    public EmbedBuilder buildEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setFooter("Press the buttons below to edit or cancel your trade.", Bot.jda.getSelfUser().getEffectiveAvatarUrl());
        embedBuilder.setAuthor(getProposerUser().getName(), null, getProposerUser().getEffectiveAvatarUrl());
        embedBuilder.setDescription(getProposerUser().getAsMention() + "**'s Offer:**\n\n" +
                "**[What " + getTargetUser().getAsMention() + " will get:](" + Bot.PATREON_LINK + ")**\n" +
                getProposerOffer().getOfferString() + "\n\n" +
                "**[What " + getProposerUser().getAsMention() + " will get:](" + Bot.PATREON_LINK + ")**\n" +
                getTargetToGiveOffer().getOfferString());
        return embedBuilder;
    }
}
