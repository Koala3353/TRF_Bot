package com.general_hello.commands.commands.Auction;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.HashMap;

public class QuestionProgress {
    public static HashMap<Member, Progress> progress = new HashMap<>();
    public static HashMap<Member, OffsetDateTime> time = new HashMap<>();
    public static HashMap<Member, String> item = new HashMap<>();
    public static HashMap<Member, TextChannel> mainChannel = new HashMap<>();
    public static HashMap<Member, Long> minBid = new HashMap<>();
    public static HashMap<Member, Long> maxBid = new HashMap<>();
    public static HashMap<Member, Role> allowedRole = new HashMap<>();
    public static HashMap<Member, TextChannel> logChannel = new HashMap<>();
}
