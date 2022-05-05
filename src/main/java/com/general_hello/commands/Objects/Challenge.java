package com.general_hello.commands.Objects;

import com.general_hello.Bot;
import com.general_hello.commands.Objects.Level.Rank;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.general_hello.Config.get;

public class Challenge {
    private static final HashMap<Long, Challenge> challenges = new HashMap<>();
    private static final HashMap<Rank, ArrayList<Challenge>> rankChallenges = new HashMap<>();
    private boolean isAccepted;
    private final boolean userOnly;
    private final boolean rankOnly;
    private final boolean global;
    private final Player targetedPlayer;
    private final Rank targetedRank;
    private final Player challenger;
    private Player acceptor;
    private String messagelink;
    private boolean resultSetChallenger;
    private boolean resultSetAcceptor;
    private Player winner;

    public Challenge(long challenger, Player targetedPlayer, String roleName) {
        this.targetedPlayer = targetedPlayer;
        this.targetedRank = null;
        this.isAccepted = false;
        this.userOnly = true;
        this.rankOnly = false;
        this.challenger = new Player(challenger, roleName);
        this.acceptor = null;
        this.global = false;
        this.messagelink = "";
        this.resultSetChallenger = false;
        this.resultSetAcceptor = false;
        this.winner = null;
    }

    public Challenge(long challenger, String roleName) {
        this.targetedPlayer = null;
        this.targetedRank = null;
        this.isAccepted = false;
        this.userOnly = true;
        this.rankOnly = false;
        this.challenger = new Player(challenger, roleName);
        this.acceptor = null;
        this.global = true;
        this.messagelink = "";
        this.resultSetChallenger = false;
        this.resultSetAcceptor = false;
        this.winner = null;
    }

    public Challenge(long challenger, Rank targetedRank, String roleName) {
        this.targetedPlayer = null;
        this.targetedRank = targetedRank;
        this.isAccepted = false;
        this.userOnly = false;
        this.rankOnly = true;
        this.challenger = new Player(challenger, roleName);
        this.acceptor = null;
        this.global = false;
        this.messagelink = "";
        this.resultSetChallenger = false;
        this.resultSetAcceptor = false;
        this.winner = null;
    }

    public static Challenge getChallenge(long userId) {
        return challenges.get((userId));
    }

    public static ArrayList<Challenge> getChallenges() {
        return new ArrayList<>(challenges.values());
    }

    public void delete() {
        challenges.remove(this.challenger.getUserid());
        if (this.acceptor != null) challenges.remove(this.acceptor.getUserid(), this);
        if (this.rankOnly) {
            ArrayList<Challenge> challenges = rankChallenges.get(this.targetedRank);
            challenges.remove(this);
            rankChallenges.put(this.targetedRank, challenges);
        }
    }

    public boolean isResultSetChallenger() {
        return resultSetChallenger;
    }

    public boolean isResultSetAcceptor() {
        return resultSetAcceptor;
    }

    public Challenge setResultSetChallenger(boolean resultSetChallenger) {
        this.resultSetChallenger = resultSetChallenger;
        return this;
    }

    public Challenge setResultSetAcceptor(boolean resultSetAcceptor) {
        this.resultSetAcceptor = resultSetAcceptor;
        return this;
    }

    public static boolean hasChallenge(long userId) {
        return challenges.containsKey((userId));
    }

    public static ArrayList<Challenge> getRankChallenge(Rank rank) {
        return rankChallenges.get(rank);
    }

    public Challenge save() {
        challenges.put(this.challenger.getUserid(), this);
        if (this.acceptor != null) challenges.put(this.acceptor.getUserid(), this);
        if (this.rankOnly) {
            if (!rankChallenges.containsKey(this.targetedRank)) {
                rankChallenges.put(this.targetedRank, new ArrayList<>(List.of(this)));
            } else {
                ArrayList<Challenge> updatedList = rankChallenges.get(this.targetedRank);
                updatedList.add(this);
                rankChallenges.put(this.targetedRank, updatedList);
            }
        }
        return this;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public boolean isUserOnly() {
        return userOnly;
    }

    public boolean isRankOnly() {
        return rankOnly;
    }

    public boolean isGlobal() {
        return global;
    }

    public Player getTargetedPlayer() {
        return targetedPlayer;
    }

    public Rank getTargetedRank() {
        return targetedRank;
    }

    public Player getChallenger() {
        return challenger;
    }

    public Player getAcceptor() {
        return acceptor;
    }

    public Challenge setAccepted(boolean accepted) {
        isAccepted = accepted;
        return this;
    }

    public Challenge setAcceptor(long acceptor, String roleName) {
        this.acceptor = new Player(acceptor, roleName);
        return this;
    }

    public String challengeType() {
        if (isGlobal()) {
            return "Anyone";
        }

        if (isRankOnly()) {
            return "Specific rank";
        }

        return "Specific user";
    }

    // Makes the embed of the challenge
    public EmbedBuilder buildEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setFooter("Press the buttons to accept the challenge", Bot.jda.getSelfUser().getEffectiveAvatarUrl());
        embedBuilder.setAuthor(getChallenger().getUser().getName(), null, getChallenger().getUser().getEffectiveAvatarUrl());
        embedBuilder.setDescription(getChallenger().getUser().getAsMention() + "**'s Challenge:**\n" +
                "[**Rank:**](" + get("link") + ") " + Rank.getRankFromPoints(getChallenger().getPoints()).getName() + "\n" +
                "[**Points:**](" + get("link") + ") " + getChallenger().getFormattedPoints() + "\n\n" +
                "[**Challenge Type:**](" + get("link") + ") " + challengeType() + "\n" +
                (isRankOnly() ? "[**Specific Rank:**](" + get("link") + ") " + getTargetedRank().getName() : ""));
        return embedBuilder;
    }

    public EmbedBuilder buildEmbedApproved() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setFooter("Press the buttons to set the results", Bot.jda.getSelfUser().getEffectiveAvatarUrl());
        embedBuilder.setAuthor(getChallenger().getUser().getName(), null, getChallenger().getUser().getEffectiveAvatarUrl());
        embedBuilder.setDescription(getChallenger().getUser().getAsMention() + "**'s (" + getChallenger().getUserid() + ") Challenge:**\n" +
                "[**Rank:**](" + get("link") + ") " + Rank.getRankFromPoints(getChallenger().getPoints()).getName() + "\n" +
                "[**Points:**](" + get("link") + ") " + getChallenger().getFormattedPoints() + "\n\n" +
                "[**Challenge Type:**](" + get("link") + ") " + challengeType() + "\n" +
                (isRankOnly() ? "[**Specific Rank:**](" + get("link") + ") " + getTargetedRank().getName() : "") + "\n\n" +
                "[**Accepted by:**](" + get("link") + ") " + getAcceptor().getUser().getAsMention() + " (" + getAcceptor().getUserid() + ")\n" +
                "[**Rank:**](" + get("link") + ") " + Rank.getRankFromPoints(getAcceptor().getPoints()).getName() + "\n" +
                "[**Points:**](" + get("link") + ") " + getAcceptor().getFormattedPoints());
        return embedBuilder;
    }

    public Challenge setMessagelink(String messagelink) {
        this.messagelink = messagelink;
        return this;
    }

    public String getMessagelink() {
        return messagelink;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getLoser() {
        return winner.getUserid() == getChallenger().getUserid() ? getAcceptor() : getChallenger();
    }

    public Challenge setWinner(Player winner) {
        this.winner = winner;
        return this;
    }
}
