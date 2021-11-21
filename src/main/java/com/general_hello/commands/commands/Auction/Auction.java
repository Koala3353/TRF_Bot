package com.general_hello.commands.commands.Auction;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class Auction {
    private OffsetDateTime time;
    private final String item;
    private ArrayList<User> bidders;
    private ArrayList<Long> bid;
    private final TextChannel mainChannel;
    private final long minBid;
    private final long maxBid;
    private final Role allowedRole;
    private final TextChannel logChannel;

    public Auction(OffsetDateTime time, String item, TextChannel mainChannel, long minBid, long maxBid, Role allowedRole, TextChannel logChannel) {
        this.time = time;
        this.item = item;
        this.bidders = new ArrayList<>();
        this.bid = new ArrayList<>();
        this.mainChannel = mainChannel;
        this.minBid = minBid;
        this.maxBid = maxBid;
        this.allowedRole = allowedRole;
        this.logChannel = logChannel;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public Auction setTime(OffsetDateTime time) {
        this.time = time;
        return this;
    }

    public String getItem() {
        return item;
    }

    public ArrayList<User> getBidders() {
        return bidders;
    }

    public Auction setBidders(ArrayList<User> bidders) {
        this.bidders = bidders;
        return this;
    }

    @Deprecated
    public Auction addBidders(User bidders) {
        this.bidders.add(bidders);
        return this;
    }

    public Auction newBidders(int auctionId, User bidder, Long bid) {
        this.bidders.add(bidder);
        this.bid.add(bid);
        return this;
    }

    public ArrayList<Long> getBid() {
        return bid;
    }

    @Deprecated
    public Auction setBid(ArrayList<Long> bid) {
        this.bid = bid;
        return this;
    }

    @Deprecated
    public Auction addBid(long bid) {
        this.bid.add(bid);
        return this;
    }

    public TextChannel getMainChannel() {
        return mainChannel;
    }

    public long getMinBid() {
        return minBid;
    }

    public long getMaxBid() {
        return maxBid;
    }

    public Role getAllowedRole() {
        return allowedRole;
    }

    public TextChannel getLogChannel() {
        return logChannel;
    }
}
