package com.general_hello.commands.commands.Auction;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class AuctionToChannelStoring {
    public static HashMap<TextChannel, Auction> textChannelAuctionHashMap = new HashMap<>();
    public static HashMap<User, String> bidToUser = new HashMap<>();
    public static HashMap<Integer, Auction> idAuctionHashMap = new HashMap<>();
}